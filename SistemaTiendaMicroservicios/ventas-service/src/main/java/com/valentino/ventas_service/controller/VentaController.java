package com.valentino.ventas_service.controller;

import com.valentino.ventas_service.dto.VentaDTO;
import com.valentino.ventas_service.model.Venta;
import com.valentino.ventas_service.service.IVentaService;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private IVentaService ventaService;

    @GetMapping
    public ResponseEntity<?> getVentas() {
        try {
            List<Venta> ventasList = ventaService.getVentas();
            return ResponseEntity.ok(ventasList);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id_venta}/{id_carrito}")
    public ResponseEntity<?> getVenta(@PathVariable Long id_venta,
                                      @PathVariable Long id_carrito) {
        try {
            VentaDTO venta = ventaService.getVenta(id_venta, id_carrito);
            return ResponseEntity.ok(venta);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/crear/{fecha_venta}/{id_carrito}")
    public ResponseEntity<?> saveVenta(@PathVariable String fecha_venta,
                                       @PathVariable Long id_carrito) {
        try {
            LocalDate fechaVenta = LocalDate.parse(fecha_venta);
            ventaService.saveVenta(fechaVenta, id_carrito);
            return ResponseEntity.ok("Venta creada con éxito");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }

    }
}
