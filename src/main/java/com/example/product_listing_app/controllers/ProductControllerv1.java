package com.example.product_listing_app.controllers;


import com.example.product_listing_app.models.dto.ProductRequestDTO;
import com.example.product_listing_app.models.dto.ProductReponseDTO;
import com.example.product_listing_app.models.dto.UpdateProductDTO;
import com.example.product_listing_app.service.ProductServiceAPI;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products")
public class ProductControllerv1 {

    Logger logger = LoggerFactory.getLogger(ProductControllerv1.class);
    @Autowired
    public ProductServiceAPI productService;

    @PostMapping()
    public ResponseEntity<String> addProduct(@RequestBody @Valid ProductRequestDTO productDTO) {
        try {
            productService.addProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
        }
        catch (Exception e) {
            logger.error("Error adding product: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error adding product");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductRequestDTO> getProductByID(@PathVariable String id) {
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        }
        catch (Exception e) {
            logger.error("Error getting product: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public ResponseEntity<List<ProductReponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        }
        catch (Exception e) {
            // TO DO: Add custom exception handling
            logger.error("Error deleting product: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error deleting product");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductReponseDTO> updateProductPrice(@PathVariable String id, @RequestBody @Valid UpdateProductDTO updateProductDTO) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, updateProductDTO));
        }
        catch (Exception e) {
            logger.error("Error updating product: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
