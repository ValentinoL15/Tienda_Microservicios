package com.valentino.carrito_service.service;

import com.valentino.carrito_service.dto.CarritoDTO;
import com.valentino.carrito_service.model.Carrito;

import java.util.List;

public interface ICarritoService {

    public List<Carrito> getCarritos();

    public Carrito getCarrito(Long id_carrito);

    public void saveCarrito(CarritoDTO carritoDTO);

    public void saveFirstCarrito(Long user_id);

    public void deleteCarrito(Long id);

}
