package com.valentino.carrito_service.dto;

import java.util.List;
import java.util.Set;

public record CarritoDTO(Long carrito_id,
                        Double total,
                         List<CarritoItemsDTO> items) {
}
