package com.example.inventory_management_system.repository;

import com.example.inventory_management_system.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name,
            String description,
            Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.quantityInStock <= p.lowStockThreshold")
    Page<Product> findLowStockProducts(Pageable pageable);

    @Query("""
            SELECT p FROM Product p
            WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Page<Product> searchProducts(
            @Param("search") String search,
            Pageable pageable);

    @Query("""
            SELECT p FROM Product p
            WHERE p.quantityInStock <= p.lowStockThreshold
              AND (
                  LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))
                  OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))
              )
            """)
    Page<Product> searchLowStockProducts(
            @Param("search") String search,
            Pageable pageable);
}

