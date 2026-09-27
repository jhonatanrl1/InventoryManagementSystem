package com.example.inventory_management_system;

import com.example.inventory_management_system.controller.PurchaseController;
import com.example.inventory_management_system.model.Product;
import com.example.inventory_management_system.model.Purchase;
import com.example.inventory_management_system.model.PurchaseItem;
import com.example.inventory_management_system.model.Supplier;
import com.example.inventory_management_system.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class PurchaseControllerTest {

    private final PurchaseService purchaseService =
            mock(PurchaseService.class);

    private final PurchaseController purchaseController =
            new PurchaseController(purchaseService);

    private final MockMvc mockMvc =
            MockMvcBuilders.standaloneSetup(purchaseController).build();

    @Test
    void createPurchaseReturnsCreatedPurchase() throws Exception {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");

        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setProduct(product);
        purchaseItem.setQuantity(5);
        purchaseItem.setPurchasePrice(new BigDecimal("45.00"));

        Purchase purchase = new Purchase();
        purchase.setPurchaseId(1L);
        purchase.setSupplier(supplier);
        purchase.setPurchaseDate(
                LocalDateTime.of(2026, 9, 18, 13, 0)
        );
        purchase.setPurchaseItems(List.of(purchaseItem));

        when(purchaseService.createPurchase(any(Purchase.class)))
                .thenReturn(purchase);

        mockMvc.perform(post("/purchases")
                        .contentType(APPLICATION_JSON)
                        .content("""
                            {
                                "supplier": {
                                    "supplierId": 2
                                },
                                "purchaseDate": "2026-09-18T13:00:00",
                                "purchaseItems": [
                                    {
                                        "product": {
                                            "productId": 2
                                        },
                                        "quantity": 5,
                                        "purchasePrice": 45.00
                                    }
                                ]
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseId").value(1))
                .andExpect(jsonPath("$.supplier.supplierId").value(2))
                .andExpect(jsonPath("$.purchaseItems[0].product.productId").value(2))
                .andExpect(jsonPath("$.purchaseItems[0].quantity").value(5))
                .andExpect(jsonPath("$.purchaseItems[0].purchasePrice").value(45.00));
    }


    @Test
    void getAllPurchasesReturnsPurchases() throws Exception {

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");

        Purchase purchase = new Purchase();
        purchase.setPurchaseId(1L);
        purchase.setSupplier(supplier);
        purchase.setPurchaseDate(
                LocalDateTime.of(2026, 9, 18, 13, 0)
        );

        when(purchaseService.getAllPurchases())
                .thenReturn(List.of(purchase));

        mockMvc.perform(get("/purchases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].purchaseId").value(1))
                .andExpect(jsonPath("$[0].supplier.supplierId").value(2))
                .andExpect(jsonPath("$[0].supplier.name")
                        .value("TechSource Distributors"));
    }



    @Test
    void getPurchaseReturnsPurchase() throws Exception {

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");

        Purchase purchase = new Purchase();
        purchase.setPurchaseId(1L);
        purchase.setSupplier(supplier);
        purchase.setPurchaseDate(
                LocalDateTime.of(2026, 9, 18, 13, 0)
        );

        when(purchaseService.getPurchase(1L))
                .thenReturn(purchase);

        mockMvc.perform(get("/purchases/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchaseId").value(1))
                .andExpect(jsonPath("$.supplier.supplierId").value(2))
                .andExpect(jsonPath("$.supplier.name")
                        .value("TechSource Distributors"));
    }




}


