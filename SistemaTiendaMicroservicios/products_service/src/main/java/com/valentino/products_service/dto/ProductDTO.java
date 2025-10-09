package com.valentino.products_service.dto;

public record ProductDTO(String product_name,
                         String brand,
                         Integer quantity,
                         Double price) {
}
