package com.example.inventory_management_system.repository;



import com.example.inventory_management_system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p FROM Product p WHERE p.quantityInStock <= p.lowStockThreshold")
    List<Product> findLowStockProducts();

}

