package com.example.product_listing_app;

import com.example.product_listing_app.controllers.ProductControllerv1;
import com.example.product_listing_app.models.dto.ProductRequestDTO;
import com.example.product_listing_app.models.dto.ProductReponseDTO;
import com.example.product_listing_app.models.dto.UpdateProductDTO;
import com.example.product_listing_app.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)  // Ensures Mockito works with JUnit 5
class ProductControllerV1Test {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;  // Mock the service to avoid using the actual service

    @InjectMocks
    private ProductControllerv1 productController;  // Inject the mocked service into the controller

    private ProductRequestDTO productDTO;
    private UpdateProductDTO updateProductDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();  // Set up MockMvc for testing the controller

        // Set up sample ProductDTO and UpdateProductDTO
        productDTO = new ProductRequestDTO();
        productDTO.setName("Test Product");
        productDTO.setPrice(99.99f);
    }

    @Test
    void addProduct_ShouldReturn201_WhenValidProduct() throws Exception {
        // Mock the service call for adding a product
        doNothing().when(productService).addProduct(any(ProductRequestDTO.class));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Product added successfully"));
    }

    @Test
    void addProduct_ShouldReturn500_WhenErrorAddingProduct() throws Exception {
        // Mock the service call for adding a product
        doThrow(new RuntimeException("Error adding product")).when(productService).addProduct(any(ProductRequestDTO.class));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Error adding product"));
    }

    // Test validation
//    @Test
//    void addProduct_InvalidateProductDTO_ShouldReturn400() throws Exception {
//        // Set up an invalid ProductDTO
//        ProductDTO invalidProductDTO = new ProductDTO();
//        invalidProductDTO.setName("T");
//        invalidProductDTO.setPrice(-99.99f);
//
//        mockMvc.perform(post("/api/v1/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(invalidProductDTO)))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    void getProductByID_ShouldReturnProduct_WhenExists() throws Exception {
        // Mock the service to return a product by ID
        when(productService.getProductById("1")).thenReturn(productDTO);
        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(99.99f));
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() throws Exception {
        // Return products
        List< ProductReponseDTO> products = new ArrayList<>();
        products.add(new ProductReponseDTO("1", "Test Product 1", 99.99f));
        products.add(new ProductReponseDTO("2", "Test Product 2", 199.99f));

        // Mock the service to return a list of products
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void deleteProduct_ShouldReturn200_WhenSuccessful() throws Exception {
        // Mock the service call for deleting a product
        doNothing().when(productService).deleteProduct("1");

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));
    }

    @Test
    void updateProductPrice_ShouldReturnUpdatedProduct() throws Exception {
        // Mock the service to return an updated product
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setPrice(99.99f);

        ProductReponseDTO responseDTO = new ProductReponseDTO();
        responseDTO.setPrice(100.00f);


        when(productService.updateProduct(eq("1"), any(UpdateProductDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(patch("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(100.00f));
    }


}

