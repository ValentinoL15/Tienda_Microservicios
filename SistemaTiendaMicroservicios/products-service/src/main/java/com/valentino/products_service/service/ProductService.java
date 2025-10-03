package com.valentino.products_service.service;

import com.valentino.products_service.dto.CreateProductDTO;
import com.valentino.products_service.dto.ProductDTO;
import com.valentino.products_service.exceptions.BadRequestException;
import com.valentino.products_service.model.Product;
import com.valentino.products_service.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService{

    @Autowired
    private IProductRepository productRepository;

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("No se encontró el producto"));
    }

    @Override
    public void saveProduct(ProductDTO productDTO) {

        if(productDTO.product_name() == null) {
            throw new BadRequestException("Debes colocar un nombre al producto");
        }
        if(productDTO.brand() == null) {
            throw new BadRequestException("Debes colocarle una marca al producto");
        }
        if(productDTO.quantity() == null || productDTO.quantity() < 0) {
            throw new BadRequestException("Debes colocar una cantidad válida");
        }
        if(productDTO.price() == null || productDTO.price() <= 0) {
            throw new BadRequestException("El precio no puede ser nulo ni menor a 0");
        }

        Product product = new Product();
        product.setProduct_name(productDTO.product_name());
        product.setBrand(productDTO.brand());
        product.setPrice(productDTO.price());
        product.setQuantity(productDTO.quantity());
        productRepository.save(product);
    }

    @Override
    public void editProduct(Long id, ProductDTO product) {

        Product producto = productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("No se encuentra el producto"));

        if(product.product_name() != null) {
            producto.setProduct_name(product.product_name());
        }
        if(product.brand() != null) {
            producto.setBrand(product.brand());
        }
        if(product.price() != null) {
            if(product.price() <= 0) {
                throw new BadRequestException("No pudes poner un producto menor o igual a cero");
            }
            producto.setPrice(product.price());
        }
        if(product.quantity() != null) {
            if(product.quantity() < 0) {
                throw new BadRequestException("Debes ingresar un número válido");
            }
            producto.setQuantity(product.quantity());
        }

        productRepository.save(producto);

    }

    @Override
    public void restarStock(Long product_id,int num) {
        Product product = productRepository.findById(product_id)
                .orElseThrow(() -> new RuntimeException("No se encuentra el producto"));

        Integer cantidad = product.getQuantity() - num;
        if(cantidad < 0) {
            throw new BadRequestException("No hay suficiente " + product.getProduct_name() + ", elige una menor cantidad");
        }
        product.setQuantity(cantidad);
        productRepository.save(product);
    }

    @Override
    public void restaurarStock(Long product_id, int num) {
        List<Product> products = productRepository.findAll();

        for(Product product: products) {
            if(product.getProduct_id().equals(product_id)){
                product.setQuantity(product.getQuantity() + num);
                productRepository.save(product);
            }
        }
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
