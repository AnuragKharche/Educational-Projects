package com.eparkingsolution.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.UserRepository;

import java.util.List;

@Controller
public class SiteAdministratorManageAccountsController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/viewAccounts")
    public String viewAccounts(Model model) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("userList", userList);
        return "SiteAdministratorViewAccounts";
    }

    @PostMapping("/enable/{id}")
    public String enableUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        user.setEnabled(true);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "User enabled successfully.");
        return "redirect:/viewAccounts";
    }

    @PostMapping("/disable/{id}")
    public String disableUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        user.setEnabled(false);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "User disabled successfully.");
        return "redirect:/viewAccounts";
    }

    @GetMapping("/createAccounts")
    public String createAccounts(Model model) {
        // Create an empty User object to bind to the form
        User user = new User();
        model.addAttribute("user", user);
        return "SiteAdministratorCreateAccounts";
    }

    @PostMapping("/createAccounts")
    public String createAccountsSubmit(@ModelAttribute("user") User user, RedirectAttributes attributes, Model model) {
        // Check if the username is already taken
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            model.addAttribute("usernameTakenMessage", "Username already taken.");
            return "SiteAdministratorCreateAccounts";
        }

        // Set the user's role to "Parking Owner"
        user.setRole("Parking Owner");

        // Hash the password
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        // Save the user to the database using your UserRepository
        userRepository.save(user);

        attributes.addFlashAttribute("successMessage", "Account created successfully!");
        return "redirect:/createAccounts";
    }



    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        model.addAttribute("user", user);
        return "SiteAdministratorEditUser";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttributes) {
        if (!newPassword.isEmpty()) {
            // Hash the new password
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.setPassword(hashedPassword);
        }

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "User updated successfully.");
        return "redirect:/viewAccounts";
    }





}
