package com.eparkingsolution.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eparkingsolution.model.*;
import com.eparkingsolution.repository.MessageRepository;
import com.eparkingsolution.repository.TransactionRepository;
import com.eparkingsolution.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class DriverViewBookingController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MessageRepository messageRepository;


    @GetMapping("/myBookings")
    public String viewDriverBookings(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        List<Transaction> bookings = transactionRepository.findByUser(user);
        model.addAttribute("bookings", bookings);
        return "viewDriverBookings";
    }


    @GetMapping("/myBookings/{id}")
    public String DisplayTheTransaction(@PathVariable("id") long id, Model model) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            model.addAttribute("transaction", transaction.get());
            return "viewDisplayReceipt";
        } else {
            // handle the case where the transaction with the specified ID is not found
            return "transactionNotFound";
        }
    }

    @GetMapping("/contact/{userId}")
    public String displayContactForm(@PathVariable("userId") int userId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            model.addAttribute("recipient", user);
            model.addAttribute("userId", userId);
            return "contactForm";
        } else {
            // handle the case where the user with the specified ID is not found
            return "userNotFound";
        }
    }


    @PostMapping("/sendMessage/{userId}")
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
        model.addAttribute("recipient", receiver);
        redirectAttributes.addFlashAttribute("messageSent", "Message was sent");

        return "redirect:/myBookings";
    }




}
