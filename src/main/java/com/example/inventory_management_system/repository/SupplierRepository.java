package com.example.inventory_management_system.repository;

import com.example.inventory_management_system.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface SupplierRepository extends JpaRepository<Supplier, Long> {


    List<Supplier>
    findByNameContainingIgnoreCaseOrContactNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String name,
            String contactName,
            String email);

    List<Supplier> findByNormalizedPhoneContaining(String normalizedPhone);


}

