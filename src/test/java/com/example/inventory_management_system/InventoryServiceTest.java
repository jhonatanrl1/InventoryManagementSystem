package com.example.inventory_management_system;

import com.example.inventory_management_system.model.Product;
import com.example.inventory_management_system.repository.ProductRepository;
import com.example.inventory_management_system.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void calculateInventoryValueReturnsCorrectTotal() {

        Product product = new Product();
        product.setSellingPrice(new BigDecimal("79.99"));
        product.setQuantityInStock(10);

        when(productRepository.findAll())
                .thenReturn(List.of(product));

        BigDecimal result = inventoryService.calculateInventoryValue();

        assertEquals(new BigDecimal("799.90"), result);
    }


    @Test
    void calculateInventoryValueReturnsCorrectTotalForMultipleProducts() {

        Product keyboard = new Product();
        keyboard.setSellingPrice(new BigDecimal("79.99"));
        keyboard.setQuantityInStock(10);

        Product mouse = new Product();
        mouse.setSellingPrice(new BigDecimal("29.99"));
        mouse.setQuantityInStock(5);

        when(productRepository.findAll())
                .thenReturn(List.of(keyboard, mouse));

        BigDecimal result = inventoryService.calculateInventoryValue();

        assertEquals(new BigDecimal("949.85"), result);
    }




}