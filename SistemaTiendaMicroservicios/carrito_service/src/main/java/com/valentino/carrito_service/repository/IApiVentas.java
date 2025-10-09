package com.valentino.carrito_service.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@FeignClient(name = "ventas-service")
public interface IApiVentas {

    @PostMapping("/ventas/crear/{fecha_venta}/{id_carrito}")
    public void saveVenta(@PathVariable ("fecha_venta") String fecha_venta,
                          @PathVariable ("id_carrito") Long id_carrito);

}
