package com.example.inventory_management_system.repository;


import com.example.inventory_management_system.model.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
}


