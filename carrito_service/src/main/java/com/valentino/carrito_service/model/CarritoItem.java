package com.valentino.carrito_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carrito_items")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carritoItem;

    private Long product_id;
    private Integer cantidad;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;
}
