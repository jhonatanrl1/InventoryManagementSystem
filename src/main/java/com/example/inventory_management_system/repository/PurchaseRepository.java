package com.example.inventory_management_system.repository;


import com.example.inventory_management_system.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    boolean existsBySupplier_SupplierId(Long supplierId);


    Page<Purchase> findAll(Pageable pageable);

}

