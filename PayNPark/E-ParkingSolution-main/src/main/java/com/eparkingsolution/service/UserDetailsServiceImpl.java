package com.eparkingsolution.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.MyUserDetails;
import com.eparkingsolution.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("Wrong user or password");
        }





        return new MyUserDetails(user);
    }
    public void registerUser(String username, String password, String address) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
//        user.setRole("USER");
        user.setAddress(address);
        userRepository.save(user);
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean isUsernameAvailable(String username) {
        User user = userRepository.findByUsername(username);
        return user == null;
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

}
