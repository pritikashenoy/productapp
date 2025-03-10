package com.example.product_listing_app.controllers;


import com.example.product_listing_app.models.dto.LoginRequestDTO;
import com.example.product_listing_app.models.dto.RegisterUserDTO;
import com.example.product_listing_app.service.UserService;
import com.example.product_listing_app.utils.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// TO DO: Api versioning
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationControllerv1 {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    Logger logger = LoggerFactory.getLogger(AuthenticationControllerv1.class);


    @PostMapping(value = "/login", consumes = "application/json")
    @Operation(summary = "Login a user", description = "Login a user using username and password")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDTO loginUser) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginUser.getUsername(), loginUser.getPassword());
            // Authenticate the user
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);

            // Generate the token
            UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
            String token = JWTUtil.generateToken(authenticatedUser.getUsername());

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            logger.error("Error authenticating user: {}", e.getMessage());
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping(value = "/register", consumes = "application/json")
    @Operation(summary = "Register a user", description = "Register a user using username and password")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterUserDTO newUser) {

        logger.info("Registering user: {}", newUser.getUsername());

        if (userService.userExists(newUser.getUsername())) {
            return ResponseEntity.status(400).body("Username already exists");
        }
        userService.registerUser(newUser);
        return ResponseEntity.ok("User registered successfully");
    }
}
