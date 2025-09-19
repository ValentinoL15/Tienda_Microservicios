package com.valentino.carrito_service.dto;

import java.util.List;
import java.util.Set;

public record CarritoDTO(Double total,
                         List<CarritoItemsDTO> items) {
}
