package com.valentino.carrito_service.controller;

import com.netflix.discovery.converters.Auto;
import com.valentino.carrito_service.dto.CarritoDTO;
import com.valentino.carrito_service.dto.CarritoItemsDTO;
import com.valentino.carrito_service.model.Carrito;
import com.valentino.carrito_service.service.ICarritoItemService;
import com.valentino.carrito_service.service.ICarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private ICarritoService carritoService;

    @Autowired
    private ICarritoItemService carritoItemService;

    @PreAuthorize("permitAll()")
    @PostMapping("/defectCarrito/{user_id}")
    public ResponseEntity<?> createDefectCarrito(@PathVariable Long user_id) {
        carritoService.saveFirstCarrito(user_id);
        return ResponseEntity.ok("Carrito creado con éxito");
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/traer/{id_carrito}")
    public ResponseEntity<?> getCarritoById(@PathVariable Long id_carrito) {
        CarritoDTO carrito = carritoService.getCarrito(id_carrito);
        return ResponseEntity.ok(carrito);
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<?> createCarrito(@RequestBody CarritoDTO carritoDTO) {
        try {
            carritoService.saveCarrito(carritoDTO);
            return ResponseEntity.ok("Carrito creado con éxito");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/items/{id_carrito}")
    public ResponseEntity<?> addItem(@PathVariable Long id_carrito,
                                    @RequestBody List<CarritoItemsDTO> itemsDTO) {
        carritoItemService.agregarCarritoItem(id_carrito, itemsDTO);
        return ResponseEntity.ok("Carrito creado con éxito");
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/eliminarItem/{id_carrito}/{product_id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id_carrito,
                                             @PathVariable Long product_id) {
        carritoItemService.deleteItemCarrito(id_carrito,product_id);
        return ResponseEntity.ok("Items eliminados con éxito");
    }
}
