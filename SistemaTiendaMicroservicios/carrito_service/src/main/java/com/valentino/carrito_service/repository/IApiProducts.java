package com.valentino.carrito_service.repository;

import com.valentino.carrito_service.config.FeignConfig;
import com.valentino.carrito_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "products-service",configuration = FeignConfig.class)
public interface IApiProducts {

    @GetMapping("/products/myProducts")
    public List<ProductDTO> getProducts();

    @PutMapping("/products/restarStock/{product_id}/{num}")
    public void restarStock(@PathVariable("product_id") Long product_id,
                            @PathVariable("num") Integer num);

    @GetMapping("/products/{product_id}")
    public ProductDTO getProduct(@PathVariable("product_id") Long product_id);

    @PutMapping("/products/restaurarStock/{product_id}/{num}")
    public void restaurarStock(@PathVariable("product_id") Long product_id,
                               @PathVariable("num") int num);

}
