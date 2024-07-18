package com.backand.backand.servicetest;

import com.backand.backand.model.Product;
import com.backand.backand.repository.ProductRepository;
import com.backand.backand.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private List<Product> productList;
    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product(1L, "Product Test", "Description Test", 10.0);

        productList = Arrays.asList(
                new Product(1L, "Product 1", "Description One", 10.0),
                new Product(2L, "Product 2", "Description Two", 20.0)
        );
    }

    @Test
    public void testGetProductById() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1L);
        assertEquals(product.getName(), result.get().getName());
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(product);
        assertEquals(product.getName(), result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct() {
        when(productRepository.existsById(anyLong())).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = new Product(1L, "Updated Product", "Updated Description", 12.0);
        Product result = productService.updaProduct(1L, updatedProduct);
        assertEquals(updatedProduct.getName(), result.getName());
        verify(productRepository, times(1)).existsById(anyLong());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct_NotFound() {
        when(productRepository.existsById(anyLong())).thenReturn(false);

        Product updatedProduct = new Product(1L, "Updated Product", "Updated Description", 12.0);
        Product result = productService.updaProduct(1L, updatedProduct);
        assertNull(result);
        verify(productRepository, times(1)).existsById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(anyLong());

        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(anyLong());
    }
}
