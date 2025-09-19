package com.valentino.carrito_service.service;

import com.valentino.carrito_service.dto.CarritoDTO;
import com.valentino.carrito_service.dto.CarritoItemsDTO;
import com.valentino.carrito_service.dto.ProductDTO;
import com.valentino.carrito_service.dto.UserDTO;
import com.valentino.carrito_service.model.Carrito;
import com.valentino.carrito_service.repository.IApiProducts;
import com.valentino.carrito_service.repository.IApiUsers;
import com.valentino.carrito_service.repository.ICarritoRepository;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CarritoService implements ICarritoService{

    @Autowired
    private ICarritoRepository carritoRepository;

    @Autowired
    private IApiProducts apiProducts;

    @Autowired
    private IApiUsers apiUsers;

    @Override
    public List<Carrito> getCarritos() {
        return carritoRepository.findAll();
    }

    @Override
    public Carrito getCarrito(Long id_carrito) {
        return carritoRepository.findById(id_carrito)
                .orElseThrow(() -> new RuntimeException("No se encuentra el carrito"));
    }

    @Override
    public void saveCarrito(CarritoDTO carritoDTO) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<ProductDTO> listaProducts = apiProducts.getProducts();
        Set<Long> products = new HashSet<>();

        Double total = 0.0;

        for(CarritoItemsDTO product: carritoDTO.items()) {
            for(ProductDTO produ: listaProducts) {
                if(product.equals(produ.product_id())){
                    products.add(product.carritoId());
                    total += produ.price();
                }
            }
        }

        if(products.size() == 0) {
            throw new BadRequestException("No puedes agregar un carrito vacío");
        }

        Carrito carrito = new Carrito();
        //carrito.setProducts_id(products);
        carrito.setTotal(total);

        carritoRepository.save(carrito);

        //apiUsers.addCarritoToUser(carrito.getCarrito_id());


    }

    @Override
    public void saveFirstCarrito(Long user_id) {
        Carrito carrito = new Carrito();
        carritoRepository.save(carrito);
        UserDTO user = apiUsers.getUserById(user_id);
        Set<Long> carritos = user.getCarrito_id();
        carritos.add(carrito.getCarrito_id());
        user.setCarrito_id(carritos);
        apiUsers.addCarritoToUser(user_id,carrito.getCarrito_id());
    }

    @Override
    public void deleteCarrito(Long id) {

    }
}
