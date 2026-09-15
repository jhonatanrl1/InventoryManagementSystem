package com.example.inventory_management_system.repository;

import com.example.inventory_management_system.model.ProductSupplier;
import com.example.inventory_management_system.model.ProductSupplierId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, ProductSupplierId> {

}

