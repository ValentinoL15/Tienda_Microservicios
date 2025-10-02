package com.valentino.carrito_service.service;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.valentino.carrito_service.dto.CarritoDTO;
import com.valentino.carrito_service.dto.CarritoItemsDTO;
import com.valentino.carrito_service.dto.ProductDTO;
import com.valentino.carrito_service.dto.UserDTO;
import com.valentino.carrito_service.model.Carrito;
import com.valentino.carrito_service.model.CarritoItem;
import com.valentino.carrito_service.repository.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CarritoItemService implements ICarritoItemService{

    @Autowired
    private ICarritoRepository carritoRepository;

    @Autowired
    private ICarritoItemRepository carritoItemRepository;

    @Autowired
    private IApiProducts apiProducts;

    @Autowired
    private IApiUsers apiUsers;

    @Autowired
    private IApiVentas apiVentas;

    @Override
    public List<CarritoItemsDTO> getCarritoItems() {
        List<CarritoItem> carritoItems = carritoItemRepository.findAll();
        List<CarritoItemsDTO> carros =  carritoItems.stream().map(carritoItem -> new CarritoItemsDTO(
                carritoItem.getId_carritoItem(),
                carritoItem.getProduct_id(),
                carritoItem.getCantidad(),
                carritoItem.getTotal()
        )).collect(Collectors.toList());
        return carros;
    }

    @Override
    public CarritoItemsDTO getCarritoItem(Long id_carritoItem) {
        CarritoItem carritoItem = carritoItemRepository.findById(id_carritoItem)
                .orElseThrow(() -> new RuntimeException("No se enceuntra el carrito"));
        CarritoItemsDTO myCarrito = new CarritoItemsDTO(
                carritoItem.getProduct_id(),
                carritoItem.getProduct_id(),
                carritoItem.getCantidad(),
                carritoItem.getTotal()
        );
        return myCarrito;
    }

    @Override
    @CircuitBreaker(name = "users-service", fallbackMethod = "fallbackGetUsersByCarrito")
    @Retry(name = "users-service")
    public void agregarCarritoItem(Long id_carrito, List<CarritoItemsDTO> itemsDTO) {
        Carrito carrito = carritoRepository.findById(id_carrito)
                .orElseThrow(() -> new RuntimeException("No se encuentra el carrito"));

        List<CarritoItemsDTO> items = carrito.getItems()
                .stream().map(
                        item -> new CarritoItemsDTO(
                                item.getId_carritoItem(),
                                item.getProduct_id(),
                                item.getCantidad(),
                                item.getTotal()
                        )
                ).toList();

        CarritoDTO carri = new CarritoDTO(carrito.getCarrito_id(),carrito.getTotal(),items);

        System.out.println("holi" + carri.total());
        UserDTO user = apiUsers.getUserByCarrito(id_carrito);
        System.out.println(user);

        System.out.println("Mi user:" + user);
        String myUser = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("User: " + user.getUsername() + " ," + " y myUser: " + myUser);
        /*if(!user.getUsername().equals(myUser)) {
            throw new BadRequestException("No tienes los permisos para realizar esta acción");
        }*/
        System.out.println("gola");
        /*if(carri.items().size() != 0){
            throw new RuntimeException("El carrito ya ha sido utilizado");
        }*/
        System.out.println("chau");


        List<CarritoItem> nuevosItems = new ArrayList<>();
        System.out.println("Esta por entrar al for");
        for(CarritoItemsDTO dto: itemsDTO) {
            Optional<CarritoItem> itemExistente = carrito.getItems().stream()
                    .filter(item -> item.getProduct_id().equals(dto.product_id()))
                    .findFirst();


                CarritoItem nuevoItem = new CarritoItem();
                nuevoItem.setProduct_id(dto.product_id());
            System.out.println("Hasta aca llega");
                ProductDTO product = apiProducts.getProduct(dto.product_id());
            System.out.println("Lego?");
                System.out.println(product.product_name() + " " + product.price());
                nuevoItem.setCantidad(dto.cantidad());
                nuevoItem.setTotal(dto.cantidad() * product.price());
                nuevoItem.setCarrito(carrito);
                System.out.println(nuevoItem);

                carrito.getItems().add(nuevoItem);
                nuevosItems.add(nuevoItem);

        }

        // recalcular total
        double nuevoTotal = carrito.getItems().stream()
                .mapToDouble(CarritoItem::getTotal)
                .sum();
        carrito.setTotal(nuevoTotal);

        // actualizar stock de los nuevos items
        for (CarritoItem item : nuevosItems) {
            try {
                apiProducts.restarStock(item.getProduct_id(), item.getCantidad());
            } catch (ResponseStatusException ex) {
                System.out.println("Mensaje recibido: " + ex.getReason());
                throw ex;
            }

        }

        carritoRepository.save(carrito);

        Carrito newCarrito = new Carrito();
        carritoRepository.save(newCarrito);

        LocalDate fechaDeHoy = LocalDate.now();
        String fechaStr = fechaDeHoy.toString();


        Set<Long> carritos = user.getCarrito_id();
        carritos.add(newCarrito.getCarrito_id());


        user.setCarrito_id(carritos);
        apiUsers.addCarritoToUser(user.getUser_id(),newCarrito.getCarrito_id());

        apiVentas.saveVenta(fechaStr,carrito.getCarrito_id());

    }

    public void fallbackGetUsersByCarrito(Long id_carrito, List<CarritoItemsDTO> itemsDTO, Throwable throwable) {
        throw new RuntimeException("EL microservicio users no está funcionando, por favor intentalo más tarde");
    }

    public void createException() {

        throw new IllegalArgumentException("Prueba resilience y circuit breaker");

    }

    @Override
    public void deleteItemCarrito(Long id_carrito,Long product_id) {
        Carrito myCarrito = carritoRepository.findById(id_carrito)
                .orElseThrow(() -> new RuntimeException("No se encuentra el carrito"));
        System.out.println(myCarrito.getCarrito_id());

        CarritoItem itemToDelete = null;

        for(CarritoItem carri: myCarrito.getItems()){
            if(product_id.equals(carri.getProduct_id())) {
                apiProducts.restaurarStock(product_id,carri.getCantidad());
                myCarrito.setTotal(myCarrito.getTotal() - carri.getTotal());
                itemToDelete = carri;
                break; // salimos del bucle una vez encontrado
            }
        }
        if (itemToDelete != null) {
            myCarrito.getItems().remove(itemToDelete); // quitar de la lista
            carritoItemRepository.delete(itemToDelete); // borrar en la DB

            if (myCarrito.getItems().isEmpty()) {
                carritoRepository.delete(myCarrito); // borrar carrito vacío
            } else {
                carritoRepository.save(myCarrito); // guardar carrito actualizado
            }
        }
    }

    @Override
    public void editItemCarrito(Long id_carrito, Long product_id) {

    }
}
