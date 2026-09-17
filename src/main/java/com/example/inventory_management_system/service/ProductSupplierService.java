package com.example.inventory_management_system.service;

import com.example.inventory_management_system.model.ProductSupplierId;
import com.example.inventory_management_system.model.ProductSupplier;
import com.example.inventory_management_system.repository.ProductSupplierRepository;
import org.springframework.stereotype.Service;
import com.example.inventory_management_system.exception.ProductSupplierNotFoundException;

import com.example.inventory_management_system.repository.ProductRepository;
import com.example.inventory_management_system.repository.SupplierRepository;

import com.example.inventory_management_system.exception.ProductNotFoundException;
import com.example.inventory_management_system.exception.SupplierNotFoundException;

import java.util.List;

@Service
public class ProductSupplierService {

    private final ProductSupplierRepository productSupplierRepository;

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;


    public ProductSupplierService(
            ProductSupplierRepository productSupplierRepository,
            ProductRepository productRepository,
            SupplierRepository supplierRepository) {

        this.productSupplierRepository = productSupplierRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }


    public ProductSupplier createProductSupplier(ProductSupplier productSupplier) {

        Long productId = productSupplier.getProduct().getProductId();
        Long supplierId = productSupplier.getSupplier().getSupplierId();

        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found");
        }

        if (!supplierRepository.existsById(supplierId)) {
            throw new SupplierNotFoundException("Supplier not found");
        }

        return productSupplierRepository.save(productSupplier);
    }



    public List<ProductSupplier> getAllProductSuppliers() {
        return productSupplierRepository.findAll();
    }


    public ProductSupplier getProductSupplier(ProductSupplierId id) {
        return productSupplierRepository.findById(id)
                .orElseThrow(() ->
                        new ProductSupplierNotFoundException("ProductSupplier not found"));
    }


    public ProductSupplier updateProductSupplier(
            ProductSupplierId id,
            ProductSupplier productSupplier) {

        ProductSupplier existingProductSupplier =
                productSupplierRepository.findById(id)
                        .orElseThrow(() ->
                                new ProductSupplierNotFoundException("ProductSupplier not found"));

        existingProductSupplier.setPurchasePrice(
                productSupplier.getPurchasePrice()
        );

        return productSupplierRepository.save(existingProductSupplier);
    }


    public void deleteProductSupplier(ProductSupplierId id) {

        if (!productSupplierRepository.existsById(id)) {
            throw new ProductSupplierNotFoundException("ProductSupplier not found");
        }

        productSupplierRepository.deleteById(id);
    }




}



