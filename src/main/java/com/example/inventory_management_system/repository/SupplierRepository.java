package com.example.inventory_management_system.repository;

import com.example.inventory_management_system.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}

