package com.valentino.ventas_service.service;

import com.netflix.discovery.converters.Auto;
import com.valentino.ventas_service.dto.CarritoDTO;
import com.valentino.ventas_service.dto.VentaDTO;
import com.valentino.ventas_service.model.Venta;
import com.valentino.ventas_service.repository.ICarritoApi;
import com.valentino.ventas_service.repository.IVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VentaService implements IVentaService{

    @Autowired
    private IVentaRepository ventaRepository;

    @Autowired
    private ICarritoApi carritoApi;

    @Override
    public List<Venta> getVentas() {
        List<Venta> ventasList = ventaRepository.findAll();
        return ventasList;
    }

    @Override
    public VentaDTO getVenta(Long id_venta, Long id_carrito) {
        Venta venta = ventaRepository.findById(id_venta)
                .orElseThrow(() -> new RuntimeException("No se encuentra la venta"));

        CarritoDTO carrito = carritoApi.getCarrito(id_carrito);

        VentaDTO venta1 = new VentaDTO(
                venta.getId_venta(),
                venta.getFecha_venta(),
                carrito
        );

        return venta1;
    }

    @Override
    public void saveVenta(LocalDate fecha_venta, Long id_carrito) {
        Venta miVenta = new Venta();
        if(fecha_venta.equals(null)) {
            throw new RuntimeException("La venta debe tener una fecha");
        }
        if(fecha_venta != null) {
            miVenta.setFecha_venta(fecha_venta);
        }
        if(id_carrito.equals(null)) {
            throw new RuntimeException("No puedes crear una venta sin carrito");
        }
        if(id_carrito != null) {
            miVenta.setId_carrito(id_carrito);
        }
        ventaRepository.save(miVenta);
    }
}
