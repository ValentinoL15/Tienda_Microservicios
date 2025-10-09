package com.valentino.carrito_service.dto;

public record ProductDTO(Long product_id,
                         String product_name,
                         String brand,
                         Integer quantity,
                         Double price) {
}
