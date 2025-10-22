package com.cafe_be.service;

import com.cafe_be.model.Product;
import java.util.List;

public interface ProductService {
    Product create(Product p);
    Product update(Long id, Product p);
    void delete(Long id);
    List<Product> listAll();
    Product findById(Long id);
}
