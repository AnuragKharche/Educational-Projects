package com.eparkingsolution.controller;


import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eparkingsolution.model.*;
import com.eparkingsolution.repository.CardRepository;
import com.eparkingsolution.repository.ParkingSpaceRepository;
import com.eparkingsolution.repository.TransactionRepository;
import com.eparkingsolution.repository.UserRepository;
import com.eparkingsolution.service.CarParkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class BookingController {


    @Autowired
    private CarParkService carParkService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CardRepository cardRepository;


   
    @GetMapping("/booking")
    public String viewHomePage(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("address", user.getAddress());



        return findPaginated1(1, "name", "asc", model, principal);
    }

//    Pagination alone without Google Maps API
    @GetMapping("/page/{pageNo}")
    public String findPaginated1(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model,
                                Principal principal) {
        int pageSize = 5;

        Page<CarPark> page = carParkService.findPaginated(pageNo, pageSize, sortField, sortDir);

        List<CarPark> carPark = page.getContent();

        String username = principal.getName();
        User user = userRepository.findByUsername(username);


        model.addAttribute("user", user);
        model.addAttribute("address", user.getAddress());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("carPark", carPark);
        return "booking";
    }

    // handle the form submission to update the user's address
    @PostMapping("/update-address")
    public String updateAddress(Principal principal, @RequestParam("address") String address, RedirectAttributes redirectAttributes) {
        User existingUser = userRepository.findByUsername(principal.getName());
        existingUser.setAddress(address);
        userRepository.save(existingUser);
        redirectAttributes.addFlashAttribute("message", "Address updated successfully");
        return "redirect:/booking";
    }


    @GetMapping("/booking/{id}")
    public String listObjectsByForeignKey(@PathVariable("id") int id,
                                          @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                          @RequestParam(value = "sortField", defaultValue = "id") String sortField,
                                          @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
                                          Model model) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.Direction.fromString(sortDir), sortField);
        Page<ParkingSpace> page = parkingSpaceRepository.findByCarParkId(id, pageable);

        model.addAttribute("listparkingspace", page.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("carParkId", id);

        return "bookingPS";

    }



        @GetMapping("/booking/parking-space/{id_ps}")
    public String getParkingSpaceById(@PathVariable("id_ps") long id_ps, Model model) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id_ps).orElse(null);
        List<Transaction> transactions = transactionRepository.findByParkingSpace(parkingSpace);
        model.addAttribute("parkingSpace", parkingSpace);
        if (transactions.isEmpty()) {
            model.addAttribute("message", "No bookings yet for this parking space.");
        } else {
            model.addAttribute("transactions", transactions);
        }
        return "displayPS";
    }






    //    Takes input form the User and calculates the total bill to be paid
    // Check if the parking space is available during the selected time frame
    @PostMapping("/booking/parking-space/{id_ps}")
    public String bookParkingSpace(@PathVariable("id_ps") long id_ps,
                                   @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                   @RequestParam("startTime") @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
                                   @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                   @RequestParam("endTime") @DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
                                   @RequestParam("licensePlate") String licensePlate,
                                   @ModelAttribute Transaction transaction,
                                   Model model) {
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id_ps).orElse(null);
        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDateTime end = LocalDateTime.of(endDate, endTime);

        List<Transaction> existingTransactions = transactionRepository.findByParkingSpace(parkingSpace);

        LocalDateTime newStart = LocalDateTime.of(startDate, startTime);
        LocalDateTime newEnd = LocalDateTime.of(endDate, endTime);

        for (Transaction existingTransaction : existingTransactions) {
            LocalDateTime existingStart = existingTransaction.getStartDate().atTime(existingTransaction.getStartTime());
            LocalDateTime existingEnd = existingTransaction.getEndDate().atTime(existingTransaction.getEndTime());

            if (newStart.isAfter(existingStart) && newStart.isBefore(existingEnd) ||
                    newEnd.isAfter(existingStart) && newEnd.isBefore(existingEnd) ||
                    newStart.isBefore(existingStart) && newEnd.isAfter(existingEnd)) {
                model.addAttribute("errorMessage", "Sorry, the parking space is already booked for the selected time. Please choose a different time.");
                return "doubleBookingError";
            }
        }


        // handle the booking
        double pricePer30Minutes = parkingSpace.getPrice();
        long minutes = ChronoUnit.MINUTES.between(start, end);
        double totalCost = (minutes / 30) * pricePer30Minutes;
        totalCost = Math.round(totalCost * 100.0) / 100.0;
        model.addAttribute("totalCost", totalCost);
        model.addAttribute("startDate", startDate);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endDate", endDate);
        model.addAttribute("endTime", endTime);
        model.addAttribute("transaction", transaction);
        model.addAttribute("licensePlate", licensePlate);
        return "bookingCheckout";
    }

    //    Process the payment and display the result
    @PostMapping("/payment/{id_ps}")
    public String processPayment(@RequestParam("cardNumber") String cardNumber,
                                 @PathVariable("id_ps") long id_ps,
                                 @ModelAttribute("startDate") String startDate,
                                 @ModelAttribute("endDate") String endDate,
                                 @ModelAttribute("startTime") String startTime,
                                 @ModelAttribute("endTime") String endTime,
                                 @ModelAttribute("totalCost") double totalCost,
                                 @ModelAttribute("licensePlate") String licensePlate,
                                 Authentication authentication,
                                 Model model) {
        Card card = cardRepository.findByCardNumber(cardNumber);
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(id_ps).orElse(null);
        User user = userRepository.findByUsername(authentication.getName());
        if (card != null && card.isAccepted()) {
            Transaction transaction = new Transaction();
            transaction.setStartDate(LocalDate.parse(startDate));
            transaction.setEndDate(LocalDate.parse(endDate));
            transaction.setStartTime(LocalTime.parse(startTime));
            transaction.setEndTime(LocalTime.parse(endTime));
            transaction.setAmount(totalCost);
            transaction.setTransactionType("Parking Space Payment");
            transaction.setLicensePlate(licensePlate);
            transaction.setCard(card);
            transaction.setStatus("Payment accepted");

            // Create a DateTimeFormatter object with the corrected format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

// Get the current date and time
            LocalDateTime transactionDateTime = LocalDateTime.now();

// Format the transactionDateTime using the formatter
            String formattedTransactionDateTime = transactionDateTime.format(formatter);

// Set the formatted transactionDateTime to the transaction object
            transaction.setTransactionDateTime(formattedTransactionDateTime);

            transaction.setCardNumber(cardNumber);
            transaction.setUser(user);
            double randomNumber = Math.random() * 100000000;
            int receiptNumber = (int) randomNumber;
            transaction.setReceiptNumber(String.valueOf(receiptNumber));

            transaction.setParkingSpace(parkingSpace);

            // save the transaction to the database
            transactionRepository.save(transaction);
            model.addAttribute("transaction", transaction);
            return "displayReceipt";
        } else {
            model.addAttribute("errorMessage", "The payment could not be processed. Please check the card number and try again.");
            return "paymentError";
        }
    }


//    //The google Maps API alone
//    @GetMapping("/booking")
//    public String getAllCarParks(Model model) {
//        List<CarPark> carPark = carParkRepository.findAll();
//
//        // Get the logged in user's address
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByUsername(auth.getName());
//        String userAddress = user.getAddress();
//
//        if(userAddress!=null && !userAddress.isEmpty()){
//            // Set up the API context
//            GeoApiContext context = new GeoApiContext.Builder()
//                    .apiKey("API_KEY")
//                    .build();
//
//            // Create a distance matrix request
//            DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(context);
//            request.origins(userAddress);
//            request.destinations(carPark.stream().map(CarPark::getAddress).toArray(String[]::new));
//            request.units(Unit.IMPERIAL);
//
//            // Execute the request and get the distance matrix
//            try {
//                DistanceMatrix distanceMatrix = request.await();
//                // Add the distances to the car parks
//                for (int i = 0; i < carPark.size(); i++) {
//                    double distanceInMiles = distanceMatrix.rows[0].elements[i].distance.inMeters / 1609.34;
//                    carPark.get(i).setDistanceInMiles(distanceInMiles);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        model.addAttribute("carPark", carPark);
//
//        return "booking";
//    }


    // !!! Working pagination with Google Maps API
//    @GetMapping("/page/{pageNo}")
//    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
//                                @RequestParam("sortField") String sortField,
//                                @RequestParam("sortDir") String sortDir,
//                                Model model,
//                                Principal principal) {
//        int pageSize = 5;
//
//        Page<CarPark> page = carParkService.findPaginated(pageNo, pageSize, sortField, sortDir);
//        List<CarPark> carPark = page.getContent();
//        // Get the logged in user's address
//        String username = principal.getName();
//        User user = userRepository.findByUsername(username);
//        String userAddress = user.getAddress();
//
//        if(userAddress!=null && !userAddress.isEmpty()){
//            // Set up the API context
//            GeoApiContext context = new GeoApiContext.Builder()
//                    .apiKey("API_KEY")
//                    .build();
//
//            // Create a distance matrix request
//            DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(context);
//            request.origins(userAddress);
//            request.destinations(carPark.stream().map(CarPark::getAddress).toArray(String[]::new));
//            request.units(Unit.IMPERIAL);
//
//            // Execute the request and get the distance matrix
//            try {
//                DistanceMatrix distanceMatrix = request.await();
//                // Add the distances to the car parks
//                for (int i = 0; i < carPark.size(); i++) {
//                    double distanceInMiles = distanceMatrix.rows[0].elements[i].distance.inMeters / 1609.34;
//                    carPark.get(i).setDistanceInMiles(distanceInMiles);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        // Convert the carParks List to a JSON string
//        String carParkJson = "";
//        try {
//            carParkJson = new ObjectMapper().writeValueAsString(carPark);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        model.addAttribute("carParkJson", carParkJson);
//        model.addAttribute("initMap", "initMap();");
//
//
//
//        model.addAttribute("user", user);
//        model.addAttribute("address", user.getAddress());
//
//        model.addAttribute("currentPage", pageNo);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems", page.getTotalElements());
//
//        model.addAttribute("sortField", sortField);
//        model.addAttribute("sortDir", sortDir);
//        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
//
//        model.addAttribute("carPark", carPark);
//        return "booking";
//    }


}