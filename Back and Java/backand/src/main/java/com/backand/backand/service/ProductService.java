package com.backand.backand.service;

import com.backand.backand.model.Product;
import com.backand.backand.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Optional<Product> getProductById(Long id){

        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {

        return productRepository.save(product);

    }

    public Product updaProduct(Long id, Product product) {

        if(productRepository.existsById(id)) {
            product.setId(id);
            

            return productRepository.save(product);

        }
        
        return null;
    }


    public void deleteProduct(Long id) {
        
        productRepository.deleteById(id);
    }
    
}
