package com.example.inventory_management_system.service;

import com.example.inventory_management_system.model.Product;
import com.example.inventory_management_system.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InventoryService {

    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public BigDecimal calculateInventoryValue() {

        List<Product> products = productRepository.findAll();

        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product product : products) {
            BigDecimal productValue = product.getSellingPrice()
                    .multiply(BigDecimal.valueOf(product.getQuantityInStock()));

            totalValue = totalValue.add(productValue);
        }

        return totalValue;
    }
}

