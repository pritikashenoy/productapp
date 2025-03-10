package com.example.product_listing_app.models.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;


@Getter
public class UpdateProductDTO {
    @Positive(message = "Price must be positive")
    private Float price;

    public UpdateProductDTO(Float price) {
        this.price = price;
    }

    // Getter
    public Float getPrice() {
        return price;
    }

    public  void setPrice(Float price) {
        this.price = price;
    }
}
