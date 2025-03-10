package com.example.product_listing_app.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {
    // Can add more fields like email, phone number, etc.
    private String username;
    private String password;

    public RegisterUserDTO() {
    }

    public RegisterUserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
