package com.valentino.carrito_service.repository;

import com.valentino.carrito_service.config.FeignConfig;
import com.valentino.carrito_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "users-service", configuration = FeignConfig.class)
public interface IApiUsers {

    @GetMapping("/auth/getUserByName/{username}")
    public UserDTO getUserByUsername(String username);

    @GetMapping("/auth/user/{user_id}")
    public UserDTO getUserById(@PathVariable Long user_id);


    @PutMapping("/auth/{user_id}/carrito/{carritoId}")
    public void addCarritoToUser(@PathVariable("user_id") Long user_id,
                          @PathVariable("carritoId") Long carritoId);

    @GetMapping("/auth/userByCarrito/{id_carrito}")
    public UserDTO getUserByCarrito(@PathVariable("id_carrito") Long id_carrito);

}
