package com.eparkingsolution.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eparkingsolution.model.User;
import com.eparkingsolution.service.UserDetailsServiceImpl;

@Controller
public class RegisterController {

    @Autowired
    private UserDetailsServiceImpl userService;



    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPass = passwordEncoder.encode(user.getPassword());

        if(user.getUsername().isEmpty()){
            model.addAttribute("emptyUser","Email can't be empty");
            return "register";
        }
        if (userService.isUsernameTaken(user.getUsername())) {
            model.addAttribute("errorTaken", "Username already exists");
            return "register";
        }
        if(user.getPassword().isEmpty()){
            model.addAttribute("emptyPassword","Password can't be empty");
            return "register";
        }



        userService.registerUser(user.getUsername(), passwordEncoder.encode(user.getPassword()),user.getAddress());
        redirectAttributes.addFlashAttribute("messageSent", "Registered successfully!");

        return "redirect:/login";
    }



}
