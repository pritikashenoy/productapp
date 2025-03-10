package com.example.product_listing_app.models.mappers;

import com.example.product_listing_app.models.dto.ProductRequestDTO;
import com.example.product_listing_app.models.dto.ProductReponseDTO;
import com.example.product_listing_app.models.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Convert Product entity to ProductDTO
    ProductRequestDTO toDTO(Product product);

    Product toEntity(ProductRequestDTO productDTO);

    // Convert a list of Product entities to a list of ProductDTOs
    List<ProductRequestDTO> toDTOList(List<Product> products);

    // Convert Product entity to ProductReponseDTO
    ProductReponseDTO toResponseDTO(Product product);

    // Convert a list of Product entities to a list of ProductReponseDTOs
    List<ProductReponseDTO> toResponseDTOList(List<Product> products);

}
