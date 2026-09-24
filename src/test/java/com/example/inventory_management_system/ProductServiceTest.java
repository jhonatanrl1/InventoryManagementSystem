package com.example.inventory_management_system;

import com.example.inventory_management_system.model.Product;
import com.example.inventory_management_system.repository.ProductRepository;
import com.example.inventory_management_system.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.inventory_management_system.exception.ProductNotFoundException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import java.math.BigDecimal;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProductByIdReturnsProduct() {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");

        when(productRepository.findById(2L))
                .thenReturn(Optional.of(product));

        Product result = productService.getProductById(2L);

        assertEquals("Keyboard", result.getName());
    }

    @Test
    void getProductByIdThrowsExceptionWhenProductDoesNotExist() {

        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(999L)
        );
    }

    @Test
    void createProductReturnsSavedProduct() {

        Product product = new Product();
        product.setName("Mouse");
        product.setSellingPrice(new BigDecimal("29.99"));
        product.setQuantityInStock(20);
        product.setLowStockThreshold(5);

        when(productRepository.save(product))
                .thenReturn(product);

        Product result = productService.createProduct(product);

        assertEquals("Mouse", result.getName());
        assertEquals(new BigDecimal("29.99"), result.getSellingPrice());
        assertEquals(20, result.getQuantityInStock());
    }



    @Test
    void updateProductReturnsUpdatedProduct() {

        Product existingProduct = new Product();
        existingProduct.setProductId(2L);
        existingProduct.setName("Keyboard");
        existingProduct.setSellingPrice(new BigDecimal("79.99"));
        existingProduct.setQuantityInStock(10);
        existingProduct.setLowStockThreshold(3);

        Product updatedProduct = new Product();
        updatedProduct.setName("Mechanical Keyboard");
        updatedProduct.setSellingPrice(new BigDecimal("89.99"));
        updatedProduct.setQuantityInStock(15);
        updatedProduct.setLowStockThreshold(5);

        when(productRepository.findById(2L))
                .thenReturn(Optional.of(existingProduct));

        when(productRepository.save(existingProduct))
                .thenReturn(existingProduct);

        Product result = productService.updateProduct(2L, updatedProduct);

        assertEquals("Mechanical Keyboard", result.getName());
        assertEquals(new BigDecimal("89.99"), result.getSellingPrice());
        assertEquals(15, result.getQuantityInStock());
        assertEquals(5, result.getLowStockThreshold());
    }

    @Test
    void getAllProductsReturnsAllProductsWhenNoSearchIsProvided() {

        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Mouse");

        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setName("Keyboard");

        when(productRepository.findAll())
                .thenReturn(List.of(product1, product2));

        List<Product> result = productService.getAllProducts(null);

        assertEquals(2, result.size());
        assertEquals("Mouse", result.get(0).getName());
        assertEquals("Keyboard", result.get(1).getName());
    }

    @Test
    void getAllProductsReturnsMatchingProductsForSearch() {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");

        when(productRepository.findByNameContainingIgnoreCase("KEYBOARD"))
                .thenReturn(List.of(product));

        List<Product> result = productService.getAllProducts("KEYBOARD");

        assertEquals(1, result.size());
        assertEquals("Keyboard", result.get(0).getName());
    }

    @Test
    void getAllProductsReturnsEmptyListWhenSearchHasNoMatches() {

        when(productRepository.findByNameContainingIgnoreCase("zzzzzz"))
                .thenReturn(List.of());

        List<Product> result = productService.getAllProducts("zzzzzz");

        assertEquals(0, result.size());
    }

    @Test
    void getLowStockProductsReturnsLowStockProducts() {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");
        product.setQuantityInStock(2);
        product.setLowStockThreshold(3);

        when(productRepository.findLowStockProducts())
                .thenReturn(List.of(product));

        List<Product> result = productService.getLowStockProducts();

        assertEquals(1, result.size());
        assertEquals("Keyboard", result.get(0).getName());
        assertEquals(2, result.get(0).getQuantityInStock());
        assertEquals(3, result.get(0).getLowStockThreshold());
    }

}


