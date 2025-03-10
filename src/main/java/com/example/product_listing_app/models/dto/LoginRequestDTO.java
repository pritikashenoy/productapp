package com.example.product_listing_app.models.dto;

import jakarta.validation.constraints.NotNull;

public class LoginRequestDTO {

    @NotNull
    private String username;
    @NotNull
    private String password;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
