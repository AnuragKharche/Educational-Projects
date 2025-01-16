package com.eparkingsolution.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eparkingsolution.model.Message;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.MessageRepository;
import com.eparkingsolution.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ViewMessagesController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/parkingOwnerInbox")
    public String viewMessages(Authentication authentication, Model model) {
        User currentUser = userRepository.findByUsername(authentication.getName());
        List<Message> message = messageRepository.findByReceiverOrderBySentDateTimeDesc(currentUser);
        model.addAttribute("recipient", currentUser);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("message", message);
        return "inboxParkingOwner";
    }

    @GetMapping("/driverInbox")
    public String viewDriverMessages(Authentication authentication, Model model) {
        User currentUser = userRepository.findByUsername(authentication.getName());
        List<Message> message = messageRepository.findByReceiverOrderBySentDateTimeDesc(currentUser);
        model.addAttribute("recipient", currentUser);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("message", message);
        return "inboxDriver";
    }

    @GetMapping("/parkingOwnerInbox/{messageId}")
    public String viewMessage(@PathVariable Long messageId, Model model) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message == null) {
            // handle the case where the message is not found
            return "messageNotFound";
        }
        model.addAttribute("message", message);
        return "inboxParkingOwnerViewMessage";
    }

    @GetMapping("/driverInbox/{messageId}")
    public String viewDriverMessage(@PathVariable Long messageId, Model model) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message == null) {
            // handle the case where the message is not found
            return "messageNotFound";
        }
        model.addAttribute("message", message);
        return "inboxDriverViewMessage";
    }


    @GetMapping("/parkingOwnerInbox/reply/{messageId}")
    public String showReplyForm(@PathVariable Long messageId, Model model, Authentication authentication) {
        // Retrieve the recipient user and the original message
        User recipient = userRepository.findByUsername(authentication.getName());
        Message originalMessage = messageRepository.findById(messageId).orElse(null);

        if (recipient == null || originalMessage == null) {
            // Handle the case where the recipient or message are not found
            return "error";
        }

        model.addAttribute("recipient", recipient);
        model.addAttribute("originalMessage", originalMessage);
        model.addAttribute("message", new Message()); // create an empty message object

        return "contactFormPO";
    }

    @GetMapping("/driverInbox/reply/{messageId}")
    public String showReplyFormD(@PathVariable Long messageId, Model model, Authentication authentication) {
        // Retrieve the recipient user and the original message
        User recipient = userRepository.findByUsername(authentication.getName());
        Message originalMessage = messageRepository.findById(messageId).orElse(null);

        if (recipient == null || originalMessage == null) {
            // Handle the case where the recipient or message are not found
            return "error";
        }

        model.addAttribute("recipient", recipient);
        model.addAttribute("originalMessage", originalMessage);
        model.addAttribute("message", new Message()); // create an empty message object

        return "contactFormD";
    }

    @PostMapping("/parkingOwnerInbox/reply/{messageId}")
    public String submitReplyForm(@PathVariable Long messageId,
                                  @RequestParam("subject") String subject,
                                  @RequestParam("message") String message,
                                  RedirectAttributes redirectAttributes) {

        // Retrieve the recipient user and the original message
        Message originalMessage = messageRepository.findById(messageId).orElse(null);

        // Set up the reply message object
        Message newM = new Message();
        newM.setSubject("Re: " + originalMessage.getSubject());
        newM.setMessage(message);
        newM.setSender(originalMessage.getReceiver());
        newM.setReceiver(originalMessage.getSender());
        newM.setSentDateTime(LocalDateTime.now()); // set the current time as the sentDateTime

        // Save the reply message to the database
        messageRepository.save(newM);
        redirectAttributes.addFlashAttribute("messageSent", "Message was sent");

        return "redirect:/parkingOwnerInbox";
    }

    @PostMapping("/driverInbox/reply/{messageId}")
    public String submitReplyFormD(@PathVariable Long messageId,
                                  @RequestParam("subject") String subject,
                                  @RequestParam("message") String message,
                                  RedirectAttributes redirectAttributes) {

        // Retrieve the recipient user and the original message
        Message originalMessage = messageRepository.findById(messageId).orElse(null);

        // Set up the reply message object
        Message newM = new Message();
        newM.setSubject("Re: " + originalMessage.getSubject());
        newM.setMessage(message);
        newM.setSender(originalMessage.getReceiver());
        newM.setReceiver(originalMessage.getSender());
        newM.setSentDateTime(LocalDateTime.now()); // set the current time as the sentDateTime

        // Save the reply message to the database
        messageRepository.save(newM);
        redirectAttributes.addFlashAttribute("messageSent", "Message was sent");

        return "redirect:/driverInbox";
    }









}
