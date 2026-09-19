package com.example.inventory_management_system.repository;


import com.example.inventory_management_system.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

}

