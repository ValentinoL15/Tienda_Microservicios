package com.valentino.ventas_service.service;

import com.valentino.ventas_service.dto.VentaDTO;
import com.valentino.ventas_service.model.Venta;

import java.time.LocalDate;
import java.util.List;

public interface IVentaService {

    public List<Venta> getVentas();

    public VentaDTO getVenta(Long id_venta, Long id_carrito);

    public void saveVenta(LocalDate fecha_venta, Long id_carrito);



}
