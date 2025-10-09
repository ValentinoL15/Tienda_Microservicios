package com.valentino.carrito_service.repository;

import com.valentino.carrito_service.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICarritoItemRepository extends JpaRepository<CarritoItem,Long> {
}
