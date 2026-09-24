package com.example.inventory_management_system;

import com.example.inventory_management_system.model.Product;
import com.example.inventory_management_system.model.ProductSupplier;
import com.example.inventory_management_system.model.ProductSupplierId;
import com.example.inventory_management_system.model.Supplier;
import com.example.inventory_management_system.repository.ProductSupplierRepository;
import com.example.inventory_management_system.service.ProductSupplierService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.inventory_management_system.exception.ProductSupplierNotFoundException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.inventory_management_system.repository.ProductRepository;
import com.example.inventory_management_system.repository.SupplierRepository;


import com.example.inventory_management_system.exception.ProductNotFoundException;

import com.example.inventory_management_system.exception.SupplierNotFoundException;


import static org.mockito.Mockito.verify;



@ExtendWith(MockitoExtension.class)
public class ProductSupplierServiceTest {

    @Mock
    private ProductSupplierRepository productSupplierRepository;

    @InjectMocks
    private ProductSupplierService productSupplierService;


    @Mock
    private ProductRepository productRepository;

    @Mock
    private SupplierRepository supplierRepository;


    @Test
    void getProductSupplierReturnsProductSupplier() {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");

        ProductSupplierId id = new ProductSupplierId(2L, 2L);

        ProductSupplier productSupplier = new ProductSupplier();
        productSupplier.setId(id);
        productSupplier.setProduct(product);
        productSupplier.setSupplier(supplier);
        productSupplier.setPurchasePrice(new BigDecimal("45.00"));

        when(productSupplierRepository.findById(id))
                .thenReturn(Optional.of(productSupplier));

        ProductSupplier result =
                productSupplierService.getProductSupplier(id);

        assertEquals(2L, result.getId().getProduct());
        assertEquals(2L, result.getId().getSupplier());
        assertEquals("Keyboard", result.getProduct().getName());
        assertEquals("TechSource Distributors", result.getSupplier().getName());
        assertEquals(
                new BigDecimal("45.00"),
                result.getPurchasePrice()
        );
    }

    @Test
    void getProductSupplierThrowsExceptionWhenNotFound() {

        ProductSupplierId id = new ProductSupplierId(999L, 999L);

        when(productSupplierRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductSupplierNotFoundException.class,
                () -> productSupplierService.getProductSupplier(id)
        );
    }


    @Test
    void createProductSupplierReturnsSavedProductSupplier() {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");

        ProductSupplierId id = new ProductSupplierId(2L, 2L);

        ProductSupplier productSupplier = new ProductSupplier();
        productSupplier.setId(id);
        productSupplier.setProduct(product);
        productSupplier.setSupplier(supplier);
        productSupplier.setPurchasePrice(new BigDecimal("45.00"));

        when(productRepository.existsById(2L))
                .thenReturn(true);

        when(supplierRepository.existsById(2L))
                .thenReturn(true);

        when(productSupplierRepository.save(productSupplier))
                .thenReturn(productSupplier);

        ProductSupplier result =
                productSupplierService.createProductSupplier(productSupplier);

        assertEquals(2L, result.getId().getProduct());
        assertEquals(2L, result.getId().getSupplier());
        assertEquals("Keyboard", result.getProduct().getName());
        assertEquals("TechSource Distributors", result.getSupplier().getName());
        assertEquals(new BigDecimal("45.00"), result.getPurchasePrice());
    }

    @Test
    void createProductSupplierThrowsExceptionWhenProductDoesNotExist() {

        Product product = new Product();
        product.setProductId(999L);

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);

        ProductSupplier productSupplier = new ProductSupplier();
        productSupplier.setProduct(product);
        productSupplier.setSupplier(supplier);
        productSupplier.setPurchasePrice(new BigDecimal("45.00"));

        when(productRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                ProductNotFoundException.class,
                () -> productSupplierService.createProductSupplier(productSupplier)
        );
    }



    @Test
    void createProductSupplierThrowsExceptionWhenSupplierDoesNotExist() {

        Product product = new Product();
        product.setProductId(2L);

        Supplier supplier = new Supplier();
        supplier.setSupplierId(999L);

        ProductSupplier productSupplier = new ProductSupplier();
        productSupplier.setProduct(product);
        productSupplier.setSupplier(supplier);
        productSupplier.setPurchasePrice(new BigDecimal("45.00"));

        when(productRepository.existsById(2L))
                .thenReturn(true);

        when(supplierRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                SupplierNotFoundException.class,
                () -> productSupplierService.createProductSupplier(productSupplier)
        );
    }

    @Test
    void getAllProductSuppliersReturnsAllProductSuppliers() {

        Product product1 = new Product();
        product1.setProductId(2L);
        product1.setName("Keyboard");

        Supplier supplier1 = new Supplier();
        supplier1.setSupplierId(2L);
        supplier1.setName("TechSource Distributors");

        ProductSupplier productSupplier1 = new ProductSupplier();
        productSupplier1.setId(new ProductSupplierId(2L, 2L));
        productSupplier1.setProduct(product1);
        productSupplier1.setSupplier(supplier1);
        productSupplier1.setPurchasePrice(new BigDecimal("45.00"));

        Product product2 = new Product();
        product2.setProductId(3L);
        product2.setName("Mouse");

        Supplier supplier2 = new Supplier();
        supplier2.setSupplierId(3L);
        supplier2.setName("Computer Supplies Inc.");

        ProductSupplier productSupplier2 = new ProductSupplier();
        productSupplier2.setId(new ProductSupplierId(3L, 3L));
        productSupplier2.setProduct(product2);
        productSupplier2.setSupplier(supplier2);
        productSupplier2.setPurchasePrice(new BigDecimal("20.00"));

        List<ProductSupplier> productSuppliers =
                List.of(productSupplier1, productSupplier2);

        when(productSupplierRepository.findAll())
                .thenReturn(productSuppliers);

        List<ProductSupplier> result =
                productSupplierService.getAllProductSuppliers();

        assertEquals(2, result.size());
        assertEquals("Keyboard", result.get(0).getProduct().getName());
        assertEquals("TechSource Distributors",
                result.get(0).getSupplier().getName());
        assertEquals(new BigDecimal("45.00"),
                result.get(0).getPurchasePrice());

        assertEquals("Mouse", result.get(1).getProduct().getName());
        assertEquals("Computer Supplies Inc.",
                result.get(1).getSupplier().getName());
        assertEquals(new BigDecimal("20.00"),
                result.get(1).getPurchasePrice());
    }


    @Test
    void updateProductSupplierReturnsUpdatedProductSupplier() {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");

        ProductSupplierId id = new ProductSupplierId(2L, 2L);

        ProductSupplier existingProductSupplier = new ProductSupplier();
        existingProductSupplier.setId(id);
        existingProductSupplier.setProduct(product);
        existingProductSupplier.setSupplier(supplier);
        existingProductSupplier.setPurchasePrice(new BigDecimal("47.00"));

        ProductSupplier updatedProductSupplier = new ProductSupplier();
        updatedProductSupplier.setId(id);
        updatedProductSupplier.setProduct(product);
        updatedProductSupplier.setSupplier(supplier);
        updatedProductSupplier.setPurchasePrice(new BigDecimal("45.00"));

        when(productSupplierRepository.findById(id))
                .thenReturn(Optional.of(existingProductSupplier));

        when(productSupplierRepository.save(existingProductSupplier))
                .thenReturn(updatedProductSupplier);

        ProductSupplier result =
                productSupplierService.updateProductSupplier(
                        id,
                        updatedProductSupplier
                );

        assertEquals(new BigDecimal("45.00"),
                result.getPurchasePrice());

        assertEquals(2L, result.getId().getProduct());
        assertEquals(2L, result.getId().getSupplier());
    }


    @Test
    void updateProductSupplierThrowsExceptionWhenNotFound() {

        ProductSupplierId id = new ProductSupplierId(999L, 999L);

        ProductSupplier productSupplier = new ProductSupplier();
        productSupplier.setId(id);
        productSupplier.setPurchasePrice(new BigDecimal("45.00"));

        when(productSupplierRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductSupplierNotFoundException.class,
                () -> productSupplierService.updateProductSupplier(
                        id,
                        productSupplier
                )
        );
    }



    @Test
    void deleteProductSupplierRemovesProductSupplier() {

        ProductSupplierId id = new ProductSupplierId(2L, 2L);

        when(productSupplierRepository.existsById(id))
                .thenReturn(true);

        productSupplierService.deleteProductSupplier(id);

        verify(productSupplierRepository).deleteById(id);
    }


    @Test
    void deleteProductSupplierThrowsExceptionWhenNotFound() {

        ProductSupplierId id = new ProductSupplierId(999L, 999L);

        when(productSupplierRepository.existsById(id))
                .thenReturn(false);

        assertThrows(
                ProductSupplierNotFoundException.class,
                () -> productSupplierService.deleteProductSupplier(id)
        );
    }







}
