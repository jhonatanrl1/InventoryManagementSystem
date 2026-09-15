package com.example.inventory_management_system.service;

import com.example.inventory_management_system.model.ProductSupplierId;
import com.example.inventory_management_system.model.ProductSupplier;
import com.example.inventory_management_system.repository.ProductSupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSupplierService {

    private final ProductSupplierRepository productSupplierRepository;

    public ProductSupplierService(ProductSupplierRepository productSupplierRepository) {
        this.productSupplierRepository = productSupplierRepository;
    }

    public ProductSupplier createProductSupplier(ProductSupplier productSupplier) {
        return productSupplierRepository.save(productSupplier);
    }

    public List<ProductSupplier> getAllProductSuppliers() {
        return productSupplierRepository.findAll();
    }

    public ProductSupplier updateProductSupplier(
            ProductSupplierId id,
            ProductSupplier productSupplier) {

        ProductSupplier existingProductSupplier =
                productSupplierRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("ProductSupplier not found"));

        existingProductSupplier.setPurchasePrice(
                productSupplier.getPurchasePrice()
        );

        return productSupplierRepository.save(existingProductSupplier);
    }


    public void deleteProductSupplier(ProductSupplierId id) {

        if (!productSupplierRepository.existsById(id)) {
            throw new RuntimeException("ProductSupplier not found");
        }

        productSupplierRepository.deleteById(id);
    }




}



