package com.example.product_listing_app.service;

import com.example.product_listing_app.models.dto.RegisterUserDTO;
import com.example.product_listing_app.models.entity.User;
import com.example.product_listing_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    // Register a new user. The password is encoded before saving it to the database
    public void registerUser(RegisterUserDTO newUser) {
        // First encode the password
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        User user = new User(newUser.getUsername(), encodedPassword);
        userRepository.save(user);
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }


    // Override the loadUserByUsername method of UserDetailsService in Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Return a UserDetails object
        return org.springframework.security.core.userdetails.User.builder().
                username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
