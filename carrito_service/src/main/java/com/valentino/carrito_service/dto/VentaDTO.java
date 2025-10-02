package com.valentino.carrito_service.dto;

import java.time.LocalDate;

public record VentaDTO(LocalDate fecha_venta,
                       Long id_carrito) {
}
