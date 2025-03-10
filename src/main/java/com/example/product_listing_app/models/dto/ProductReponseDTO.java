package com.example.product_listing_app.models.dto;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductReponseDTO {

    private String id;
    private String name;
    private Float price;

    public ProductReponseDTO(String id, String name, Float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Float getPrice() {
        return price;
    }
}
