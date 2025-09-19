package com.valentino.users_service.dto;

public record CarritoItemsDTO(Long carritoId,
                              Long product_id,
                              int cantidad,
                              Double total) {
}
