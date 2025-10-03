package com.valentino.users_service.dto;

public record CarritoItemsDTO(Long id_carritoItem,
                              Long product_id,
                              int cantidad,
                              Double total) {
}
