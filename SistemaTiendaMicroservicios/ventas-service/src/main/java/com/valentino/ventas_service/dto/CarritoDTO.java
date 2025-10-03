package com.valentino.ventas_service.dto;

import java.util.List;

public record CarritoDTO(Long carrito_id,
                         Double total,
                         List<CarritoItemsDTO> items) {
}
