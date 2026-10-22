package com.example.inventory_management_system.repository;

import com.example.inventory_management_system.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("""
            SELECT s FROM Supplier s
            WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(s.contactName) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Page<Supplier> searchSuppliers(
            @Param("search") String search,
            Pageable pageable
    );

    Page<Supplier> findByNormalizedPhoneContaining(
            String normalizedPhone,
            Pageable pageable
    );
}

