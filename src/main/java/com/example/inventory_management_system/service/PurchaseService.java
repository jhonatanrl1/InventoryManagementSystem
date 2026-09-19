package com.example.inventory_management_system.service;

import com.example.inventory_management_system.model.Purchase;
import com.example.inventory_management_system.repository.PurchaseRepository;
import org.springframework.stereotype.Service;
import com.example.inventory_management_system.model.PurchaseItem;
import com.example.inventory_management_system.repository.ProductRepository;
import com.example.inventory_management_system.repository.SupplierRepository;
import com.example.inventory_management_system.exception.ProductNotFoundException;
import com.example.inventory_management_system.exception.SupplierNotFoundException;

import com.example.inventory_management_system.model.Supplier;
import com.example.inventory_management_system.model.Product;

import com.example.inventory_management_system.exception.PurchaseNotFoundException;

import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;



    public PurchaseService(
            PurchaseRepository purchaseRepository,
            ProductRepository productRepository,
            SupplierRepository supplierRepository) {

        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }



    public Purchase createPurchase(Purchase purchase) {


        Long supplierId = purchase.getSupplier().getSupplierId();

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() ->
                        new SupplierNotFoundException("Supplier not found"));

        purchase.setSupplier(supplier);




        if (purchase.getPurchaseItems() != null) {
            for (PurchaseItem purchaseItem : purchase.getPurchaseItems()) {


                Long productId = purchaseItem.getProduct().getProductId();

                Product product = productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ProductNotFoundException("Product not found"));

                purchaseItem.setProduct(product);



                purchaseItem.setPurchase(purchase);
            }
        }

        return purchaseRepository.save(purchase);
    }



    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public Purchase getPurchase(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() ->
                        new PurchaseNotFoundException("Purchase not found"));
    }
}




