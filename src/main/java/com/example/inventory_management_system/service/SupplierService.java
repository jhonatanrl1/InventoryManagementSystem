package com.example.inventory_management_system.service;

import com.example.inventory_management_system.exception.SupplierNotFoundException;
import com.example.inventory_management_system.model.Supplier;
import com.example.inventory_management_system.repository.ProductSupplierRepository;
import com.example.inventory_management_system.repository.PurchaseRepository;
import com.example.inventory_management_system.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public Page<Supplier> getAllSuppliers(String search, int page, int size) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page index cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("supplierId").ascending()
        );

        if (search == null || search.isBlank()) {
            return supplierRepository.findAll(pageable);
        }

        String trimmedSearch = search.trim();

        String phoneSearch = normalizePhone(trimmedSearch);

        if (phoneSearch != null) {
            return supplierRepository.findByNormalizedPhoneContaining(
                    phoneSearch,
                    pageable
            );
        }

        return supplierRepository.searchSuppliers(
                trimmedSearch,
                pageable
        );
    }

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() ->
                        new SupplierNotFoundException("Supplier not found"));
    }

    public Supplier createSupplier(Supplier supplier) {
        supplier.setNormalizedPhone(normalizePhone(supplier.getPhone()));
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier supplierDetails) {

        Supplier supplier = getSupplierById(id);

        supplier.setName(supplierDetails.getName());
        supplier.setContactName(supplierDetails.getContactName());
        supplier.setEmail(supplierDetails.getEmail());
        supplier.setPhone(supplierDetails.getPhone());
        supplier.setNormalizedPhone(normalizePhone(supplier.getPhone()));

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

    private String normalizePhone(String phone) {
        if (phone == null) {
            return null;
        }

        String digitsOnly = phone.replaceAll("\\D", "");

        return digitsOnly.isEmpty() ? null : digitsOnly;
    }
}

