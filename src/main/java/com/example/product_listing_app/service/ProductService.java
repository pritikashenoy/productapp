package com.example.product_listing_app.service;


import com.example.product_listing_app.models.dto.ProductReponseDTO;
import com.example.product_listing_app.models.dto.ProductRequestDTO;
import com.example.product_listing_app.models.mappers.ProductMapper;
import com.example.product_listing_app.models.dto.UpdateProductDTO;
import com.example.product_listing_app.models.entity.Product;
import com.example.product_listing_app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements ProductServiceAPI {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    public void addProduct(ProductRequestDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        productRepository.save(product);
    }

    public ProductRequestDTO getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        // Map to productDTO and return
        return productMapper.toDTO(product);
    }

    public List<ProductReponseDTO> getAllProducts() {
        // Add pagination
        List<Product> products = productRepository.findAll();

        return productMapper.toResponseDTOList(products);
    }

    public void deleteProduct(String id) {
        // Add log if product was not found
        productRepository.deleteById(id);
    }

    // Update product
    public ProductReponseDTO updateProduct(String id, UpdateProductDTO updateProductDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        product.setPrice(updateProductDTO.getPrice());
        productRepository.save(product);
        return productMapper.toResponseDTO(product);
    }

}
