package com.example.inventory_management_system;

import com.example.inventory_management_system.exception.ProductNotFoundException;
import com.example.inventory_management_system.exception.PurchaseNotFoundException;
import com.example.inventory_management_system.exception.SupplierNotFoundException;
import com.example.inventory_management_system.model.Product;
import com.example.inventory_management_system.model.Purchase;
import com.example.inventory_management_system.model.PurchaseItem;
import com.example.inventory_management_system.model.Supplier;
import com.example.inventory_management_system.repository.ProductRepository;
import com.example.inventory_management_system.repository.PurchaseRepository;
import com.example.inventory_management_system.repository.SupplierRepository;
import com.example.inventory_management_system.service.PurchaseService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PurchaseServiceTest {

    private final PurchaseRepository purchaseRepository =
            mock(PurchaseRepository.class);

    private final ProductRepository productRepository =
            mock(ProductRepository.class);

    private final SupplierRepository supplierRepository =
            mock(SupplierRepository.class);

    private final PurchaseService purchaseService =
            new PurchaseService(
                    purchaseRepository,
                    productRepository,
                    supplierRepository
            );

    @Test
    void createPurchaseReturnsSavedPurchase() {

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");

        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setProduct(product);
        purchaseItem.setQuantity(10);
        purchaseItem.setPurchasePrice(new BigDecimal("47.00"));

        Purchase purchase = new Purchase();
        purchase.setSupplier(supplier);
        purchase.setPurchaseItems(List.of(purchaseItem));

        when(supplierRepository.findById(2L))
                .thenReturn(Optional.of(supplier));

        when(productRepository.findById(2L))
                .thenReturn(Optional.of(product));

        when(purchaseRepository.save(purchase))
                .thenReturn(purchase);

        Purchase result = purchaseService.createPurchase(purchase);

        assertEquals(2L, result.getSupplier().getSupplierId());

        assertEquals(1, result.getPurchaseItems().size());

        assertEquals(
                2L,
                result.getPurchaseItems().get(0).getProduct().getProductId()
        );

        assertEquals(
                10,
                result.getPurchaseItems().get(0).getQuantity()
        );

        assertEquals(
                new BigDecimal("47.00"),
                result.getPurchaseItems().get(0).getPurchasePrice()
        );
    }



    @Test
    void createPurchaseThrowsExceptionWhenSupplierDoesNotExist() {

        Supplier supplier = new Supplier();
        supplier.setSupplierId(999L);

        Purchase purchase = new Purchase();
        purchase.setSupplier(supplier);

        when(supplierRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                SupplierNotFoundException.class,
                () -> purchaseService.createPurchase(purchase)
        );
    }


    @Test
    void createPurchaseThrowsExceptionWhenProductDoesNotExist() {

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);

        Product product = new Product();
        product.setProductId(999L);

        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setProduct(product);
        purchaseItem.setQuantity(10);
        purchaseItem.setPurchasePrice(new BigDecimal("47.00"));

        Purchase purchase = new Purchase();
        purchase.setSupplier(supplier);
        purchase.setPurchaseItems(List.of(purchaseItem));

        when(supplierRepository.findById(2L))
                .thenReturn(Optional.of(supplier));

        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> purchaseService.createPurchase(purchase)
        );
    }



    @Test
    void getAllPurchasesReturnsAllPurchases() {

        Purchase purchase1 = new Purchase();
        purchase1.setPurchaseId(1L);

        Purchase purchase2 = new Purchase();
        purchase2.setPurchaseId(2L);

        when(purchaseRepository.findAll())
                .thenReturn(List.of(purchase1, purchase2));

        List<Purchase> result = purchaseService.getAllPurchases();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getPurchaseId());
        assertEquals(2L, result.get(1).getPurchaseId());
    }

    @Test
    void getPurchaseReturnsPurchase() {

        Purchase purchase = new Purchase();
        purchase.setPurchaseId(1L);

        when(purchaseRepository.findById(1L))
                .thenReturn(Optional.of(purchase));

        Purchase result = purchaseService.getPurchase(1L);

        assertEquals(1L, result.getPurchaseId());
    }


    @Test
    void getPurchaseThrowsExceptionWhenNotFound() {

        when(purchaseRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                PurchaseNotFoundException.class,
                () -> purchaseService.getPurchase(999L)
        );
    }



}



