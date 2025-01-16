package com.eparkingsolution.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.eparkingsolution.model.CarPark;
import com.eparkingsolution.model.ParkingSpace;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.CarParkRepository;
import com.eparkingsolution.repository.MyUserDetails;
import com.eparkingsolution.repository.ParkingSpaceRepository;
import com.eparkingsolution.repository.UserRepository;
import com.eparkingsolution.service.CarParkService;
import com.eparkingsolution.service.ParkingSpaceService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class ManageParkingSpaceController {

    @Autowired
    private ParkingSpaceService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarParkService carParkService;
    @Autowired
    private CarParkRepository carParkRepository;


    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

//    New Parking Space
    @GetMapping("/newPS")
    public String showCreateForm(Model model, Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        List<CarPark> carParks = carParkRepository.findByUserId(user.getId());
        model.addAttribute("parkingspace", new ParkingSpace());
        model.addAttribute("carParks", carParks);
        return "createParkingSpace";
    }

//    View Parking Spaces within a certain Car Park
    @GetMapping("/manageParkingSpace/{id}")
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

        return "manageParkingSpace";
    }



    @PostMapping("/savePS")
    public String savePS(@ModelAttribute ParkingSpace parkingSpace, @RequestParam("carPark") int id_cp, @RequestParam("quantity") int quantity) {
        CarPark carPark = carParkRepository.findById(id_cp).get();
        for (int i = 0; i < quantity; i++) {
            ParkingSpace newParkingSpace = new ParkingSpace();
            newParkingSpace.setDisabledSpace(parkingSpace.getDisabledSpace());
            newParkingSpace.setFloorLevel(parkingSpace.getFloorLevel());
            newParkingSpace.setPrice(parkingSpace.getPrice());
            newParkingSpace.setCarPark(carPark);
            parkingSpaceRepository.save(newParkingSpace);
        }
        return "redirect:/manageCarPark";
    }





//Edit a Parking Space
    @RequestMapping("/editPS/{id_ps}")
    public ModelAndView showEditParkingSpacePage(@PathVariable(name = "id_ps") int id_ps){
        ModelAndView mav = new ModelAndView("createParkingSpace");
        ParkingSpace std = service.get(id_ps);
        mav.addObject("parkingspace", std);
        return mav;
    }
//Delete a Parking Space
    @RequestMapping("/deletePS/{id_ps}")
    public String deleteParkingSpace(@PathVariable(name = "id_ps") int id_ps) {
        service.delete(id_ps);
        return "redirect:/manageCarPark";
    }




}
