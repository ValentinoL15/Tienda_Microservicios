package com.valentino.carrito_service.dto;

public record CreateCarritoDTO(Long product_id,
        Integer cantidad,
        Double precio) {
}
