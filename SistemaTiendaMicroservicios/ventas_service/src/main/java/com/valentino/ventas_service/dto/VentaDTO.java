package com.valentino.ventas_service.dto;

import java.time.LocalDate;

public record VentaDTO( Long id_venta,
                        LocalDate fecha_venta,
                       CarritoDTO id_carrito) {
}
