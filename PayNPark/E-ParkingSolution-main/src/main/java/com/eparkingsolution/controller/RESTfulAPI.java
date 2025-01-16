package com.eparkingsolution.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.UserRepository;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class RESTfulAPI {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setRole("Parking Owner");
        return userRepository.save(user);
    }


    @GetMapping("/viewAll")
    public List<Map<String, Object>> getUsers() {
        List<User> users = userRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> userMap = new LinkedHashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("password", user.getPassword());
            userMap.put("address", user.getAddress());
            userMap.put("role", user.getRole());
            userMap.put("enabled", user.isEnabled());
            result.add(userMap);
        }
        return result;
    }

    @PutMapping("/enable/{id}")
    public User enableUser(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @PutMapping("/disable/{id}")
    public User disableUser(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setEnabled(false);
        return userRepository.save(user);
    }




    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class UserNotFoundException extends RuntimeException {

        public UserNotFoundException(Integer id) {
            super("User not found with ID " + id);
        }
    }


    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User user) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setAddress(user.getAddress());
        existingUser.setRole(user.getRole());
        return userRepository.save(existingUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }
}

