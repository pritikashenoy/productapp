package com.example.product_listing_app.service;

import com.example.product_listing_app.models.dto.ProductRequestDTO;
import com.example.product_listing_app.models.dto.ProductReponseDTO;
import com.example.product_listing_app.models.dto.UpdateProductDTO;

import java.util.List;

public interface ProductServiceAPI
{

    public void addProduct(ProductRequestDTO productDTO);
    public ProductRequestDTO getProductById(String id);
    public List<ProductReponseDTO> getAllProducts();
    public void deleteProduct(String id);
    public ProductReponseDTO updateProduct(String id, UpdateProductDTO updateProductDTO);
}
