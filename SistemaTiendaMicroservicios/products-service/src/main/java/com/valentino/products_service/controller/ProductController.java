package com.valentino.products_service.controller;

import com.valentino.products_service.dto.ProductDTO;
import com.valentino.products_service.exceptions.BadRequestException;
import com.valentino.products_service.model.Product;
import com.valentino.products_service.service.IProductService;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PermitAll
    @GetMapping("/myProducts")
    public ResponseEntity<List<Product>> getProducts() {
        try {
            List<Product> productsList = productService.getProducts();
            return ResponseEntity.ok(productsList);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // because the type is List<Product>
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{product_id}")
    public ResponseEntity<?> getProduct(@PathVariable Long product_id) {
        try {
            Product product = productService.getProductById(product_id);
            return ResponseEntity.ok(product);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.saveProduct(productDTO);
            return ResponseEntity.ok("Producto creado con éxito");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/restarStock/{product_id}/{num}")
    public ResponseEntity<?> restarStock(@PathVariable Long product_id,
                                         @PathVariable int num) {
        try {
            productService.restarStock(product_id,num);
            return ResponseEntity.ok("Producto restado con éxito");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }


    @PutMapping("/editar/{product_id}")
    public ResponseEntity<?> editProduct(@PathVariable Long product_id,
                                         @RequestBody ProductDTO productDTO){
        try {
            Product product = productService.getProductById(product_id);
            productService.editProduct(product.getProduct_id(),productDTO);
            return ResponseEntity.ok("EL producto ha sido modificado con éxito");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/eliminar/{product_id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long product_id) {
        try {
            productService.deleteProduct(product_id);
            return ResponseEntity.ok("Se ha eliminado el producto con éxito");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/restaurarStock/{product_id}/{num}")
    public ResponseEntity<?> restaurarStock(@PathVariable Long product_id,
                                            @PathVariable int num) {
        try {
            productService.restaurarStock(product_id,num);
            return ResponseEntity.ok("Stock restaurado");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }



}
