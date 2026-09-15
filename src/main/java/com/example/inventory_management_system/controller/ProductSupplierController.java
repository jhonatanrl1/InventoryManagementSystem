package com.example.inventory_management_system.controller;

import com.example.inventory_management_system.model.ProductSupplierId;
import com.example.inventory_management_system.model.ProductSupplier;
import com.example.inventory_management_system.service.ProductSupplierService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-suppliers")
public class ProductSupplierController {

    private final ProductSupplierService productSupplierService;

    public ProductSupplierController(ProductSupplierService productSupplierService) {
        this.productSupplierService = productSupplierService;
    }

    @PostMapping
    public ProductSupplier createProductSupplier(
            @RequestBody ProductSupplier productSupplier) {

        return productSupplierService.createProductSupplier(productSupplier);
    }

    @GetMapping
    public List<ProductSupplier> getAllProductSuppliers() {
        return productSupplierService.getAllProductSuppliers();
    }

    @PutMapping("/{productId}/{supplierId}")
    public ProductSupplier updateProductSupplier(
            @PathVariable Long productId,
            @PathVariable Long supplierId,
            @RequestBody ProductSupplier productSupplier) {

        ProductSupplierId id = new ProductSupplierId(productId, supplierId);

        return productSupplierService.updateProductSupplier(id, productSupplier);
    }


    @DeleteMapping("/{productId}/{supplierId}")
    public void deleteProductSupplier(
            @PathVariable Long productId,
            @PathVariable Long supplierId) {

        ProductSupplierId id = new ProductSupplierId(productId, supplierId);

        productSupplierService.deleteProductSupplier(id);
    }

}


