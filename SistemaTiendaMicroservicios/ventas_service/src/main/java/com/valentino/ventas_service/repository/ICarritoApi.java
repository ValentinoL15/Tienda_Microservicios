package com.valentino.ventas_service.repository;

import com.valentino.ventas_service.dto.CarritoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "carrito-service")
public interface ICarritoApi {

    @GetMapping("/carrito/traer/{id_carrito}")
    public CarritoDTO getCarrito(@PathVariable("id_carrito") Long id_carrito);

}
