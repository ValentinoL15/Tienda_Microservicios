package com.valentino.users_service.repository;

import com.valentino.users_service.config.FeignConfig;
import com.valentino.users_service.dto.CarritoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "carrito-service",configuration = FeignConfig.class)
public interface IApiCarritos {

    @PostMapping("/carrito/defectCarrito/{user_id}")
    public void createDefectCarrito(@PathVariable("user_id") Long user_id);

    @GetMapping("/carrito/traer/{id_carrito}")
    public CarritoDTO getUserByCarrito(@PathVariable("id_carrito") Long id_carrito);

}
