package com.example.inventory_management_system.service;

import com.example.inventory_management_system.model.Supplier;
import com.example.inventory_management_system.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import com.example.inventory_management_system.exception.SupplierNotFoundException;

import java.util.List;


import com.example.inventory_management_system.repository.ProductSupplierRepository;
import com.example.inventory_management_system.repository.PurchaseRepository;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;


    private final ProductSupplierRepository productSupplierRepository;
    private final PurchaseRepository purchaseRepository;



    public SupplierService(
            SupplierRepository supplierRepository,
            ProductSupplierRepository productSupplierRepository,
            PurchaseRepository purchaseRepository) {
        this.supplierRepository = supplierRepository;
        this.productSupplierRepository = productSupplierRepository;
        this.purchaseRepository = purchaseRepository;
    }



    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found"));
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier supplierDetails) {

        Supplier supplier = getSupplierById(id);

        supplier.setName(supplierDetails.getName());
        supplier.setContactName(supplierDetails.getContactName());
        supplier.setEmail(supplierDetails.getEmail());
        supplier.setPhone(supplierDetails.getPhone());

        return supplierRepository.save(supplier);
    }



    public void deleteSupplier(Long id) {
        Supplier supplier = getSupplierById(id);

        if (productSupplierRepository.existsBySupplier_SupplierId(id)) {
            throw new IllegalStateException(
                    "Cannot delete supplier because it is associated with products. "
                            + "Remove the product associations first.");
        }

        if (purchaseRepository.existsBySupplier_SupplierId(id)) {
            throw new IllegalStateException(
                    "Cannot delete supplier because it has purchase history.");
        }

        supplierRepository.delete(supplier);
    }







}

