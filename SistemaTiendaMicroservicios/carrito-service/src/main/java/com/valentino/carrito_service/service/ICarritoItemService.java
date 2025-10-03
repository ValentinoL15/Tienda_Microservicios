package com.valentino.carrito_service.service;

import com.valentino.carrito_service.dto.CarritoItemsDTO;
import com.valentino.carrito_service.model.Carrito;

import java.util.List;

public interface ICarritoItemService {

    public List<CarritoItemsDTO> getCarritoItems();

    public CarritoItemsDTO getCarritoItem(Long id_carritoItem);

    public void agregarCarritoItem(Long id_carrito, List<CarritoItemsDTO> itemsDto);

    public void deleteItemCarrito(Long id_carrito, Long product_id);

    public void editItemCarrito(Long id_carrito, Long product_id);

}
