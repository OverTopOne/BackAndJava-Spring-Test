package com.backand.backand.controllertest;


import com.backand.backand.controller.ProductController;
import com.backand.backand.model.Product;
import com.backand.backand.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

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
    public void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(productList.size()));
    }

    @Test
    public void testGetProductById() throws Exception {
        Long productId = 1L;
        when(productService.getProductById(productId)).thenReturn(Optional.of(product));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/{id}", productId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName()));
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product newProduct = new Product("New Product", "New Product Description", 15.0);
        when(productService.createProduct(any(Product.class))).thenReturn(newProduct);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"New Product\", \"description\": \"New Product Description\", \"price\": 15.0}"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(newProduct.getName()));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Long productId = 1L;
        Product updatedProduct = new Product(productId, "Updated Product", "Updated Description", 12.0);
        when(productService.updateProduct(productId, updatedProduct)).thenReturn(updatedProduct);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/{id}", productId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"Updated Product\", \"description\": \"Updated Description\", \"price\": 12.0}"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedProduct.getName()));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Long productId = 1L;
        doNothing().when(productService).deleteProduct(productId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/{id}", productId))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}