package com.eparkingsolution.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eparkingsolution.model.Message;
import com.eparkingsolution.model.Transaction;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.MessageRepository;
import com.eparkingsolution.repository.TransactionRepository;
import com.eparkingsolution.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Controller
public class ParkingOwnerViewBookingsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/parkingOwnerBookings")
    public String viewDriverBookings(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        List<Transaction> bookings = transactionRepository.findByParkingSpaceCarParkUser(user);
        model.addAttribute("bookings", bookings);
        return "viewParkingOwnerBookings";
    }

    @GetMapping("/parkingOwnerBookings/{id}")
    public String DisplayTheTransaction(@PathVariable("id") long id, Model model) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            model.addAttribute("transaction", transaction.get());
            return "viewDisplayReceiptParkingOwner";
        } else {
            // handle the case where the transaction with the specified ID is not found
            return "transactionNotFound";
        }
    }

    @GetMapping("/contactDriver/{userId}")
    public String displayContactForm(@PathVariable("userId") int userId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            model.addAttribute("recipient", user);
            model.addAttribute("userId", userId);
            return "contactFormP";
        } else {
            // handle the case where the user with the specified ID is not found
            return "userNotFound";
        }
    }

    @PostMapping("/sendMessageToDriver/{userId}")
    public String sendMessage(@RequestParam("recipient") String recipientUsername,
                              @RequestParam("subject") String subject,
                              @RequestParam("message") String message,
                              @PathVariable("userId") int userId,
                              Authentication authentication,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        User sender = userRepository.findByUsername(authentication.getName());
        User receiver = userRepository.findById(userId).orElse(null);
        if (receiver == null) {
            // handle the case where the recipient user is not found
            return "recipientNotFound";
        }
        // create a new Message object and set its properties
        Message newMessage = new Message();
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.setSubject(subject);
        newMessage.setMessage(message);
        newMessage.setSentDateTime(LocalDateTime.now());
        // save the message to the database
        messageRepository.save(newMessage);
        redirectAttributes.addFlashAttribute("messageSent", "Message was sent");

        return "redirect:/parkingOwnerBookings";
    }

    @GetMapping("/parkingOwnerBookings/edit/{id}")
    public String editTransaction(@PathVariable("id") long id, Model model) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            model.addAttribute("transaction", transaction.get());
            return "editTransaction";
        } else {
            // handle the case where the transaction with the specified ID is not found
            return "transactionNotFound";
        }
    }

    @PostMapping("/parkingOwnerBookings/edit/{id}")
    public String saveEditedTransaction(@PathVariable("id") long id, @ModelAttribute("transaction") Transaction transaction, RedirectAttributes redirectAttributes) {
        Optional<Transaction> existingTransaction = transactionRepository.findById(id);
        if (existingTransaction.isPresent()) {
            Transaction updatedTransaction = existingTransaction.get();
            updatedTransaction.setAmount(transaction.getAmount());
            updatedTransaction.setCardNumber(transaction.getCardNumber());
            updatedTransaction.setTransactionType(transaction.getTransactionType());
            updatedTransaction.setStatus(transaction.getStatus());
            updatedTransaction.setReceiptNumber(transaction.getReceiptNumber());
            updatedTransaction.setStartDate(transaction.getStartDate());
            updatedTransaction.setStartTime(transaction.getStartTime());
            updatedTransaction.setEndDate(transaction.getEndDate());
            updatedTransaction.setEndTime(transaction.getEndTime());
            updatedTransaction.setLicensePlate(transaction.getLicensePlate());
            transactionRepository.save(updatedTransaction);
            redirectAttributes.addFlashAttribute("messageEdited", "Transaction successfully edited.");
            return "redirect:/parkingOwnerBookings";
        } else {
            // handle the case where the transaction with the specified ID is not found
            return "indexParkingOwner";
        }
    }

    @GetMapping("/parkingOwnerBookings/refund/{id}")
    public String deleteTransaction(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transaction ID:" + id));
        transactionRepository.delete(transaction);
        redirectAttributes.addFlashAttribute("message", "Transaction refunded successfully.");
        return "redirect:/parkingOwnerBookings";
    }




}
