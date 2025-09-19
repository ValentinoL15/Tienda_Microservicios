package com.valentino.products_service.service;

import com.valentino.products_service.dto.ProductDTO;
import com.valentino.products_service.model.Product;

import java.util.List;

public interface IProductService {

    public List<Product> getProducts();

    public Product getProductById(Long id);

    public  void saveProduct(ProductDTO productDTO);

    public void editProduct(Long id, ProductDTO product);

    public void restarStock(Long product_id,int num);

    public void restaurarStock(Long product_id, int num);

    public void deleteProduct(Long id);

}
