package com.example.inventory_management_system;

import com.example.inventory_management_system.controller.ProductSupplierController;
import com.example.inventory_management_system.model.Product;
import com.example.inventory_management_system.model.ProductSupplier;
import com.example.inventory_management_system.model.Supplier;
import com.example.inventory_management_system.service.ProductSupplierService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.example.inventory_management_system.model.ProductSupplierId;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


public class ProductSupplierControllerTest {

    private final ProductSupplierService productSupplierService =
            mock(ProductSupplierService.class);

    private final ProductSupplierController productSupplierController =
            new ProductSupplierController(productSupplierService);

    private final MockMvc mockMvc =
            MockMvcBuilders.standaloneSetup(productSupplierController).build();

    @Test
    void createProductSupplierReturnsCreatedProductSupplier() throws Exception {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");

        ProductSupplier productSupplier = new ProductSupplier();
        productSupplier.setProduct(product);
        productSupplier.setSupplier(supplier);
        productSupplier.setPurchasePrice(new BigDecimal("45.00"));

        when(productSupplierService.createProductSupplier(any(ProductSupplier.class)))
                .thenReturn(productSupplier);

        mockMvc.perform(post("/product-suppliers")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "product": {
                                        "productId": 2
                                    },
                                    "supplier": {
                                        "supplierId": 2
                                    },
                                    "purchasePrice": 45.00
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product.productId").value(2))
                .andExpect(jsonPath("$.supplier.supplierId").value(2))
                .andExpect(jsonPath("$.purchasePrice").value(45.00));
    }



    @Test
    void getAllProductSuppliersReturnsProductSuppliers() throws Exception {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");

        ProductSupplier productSupplier = new ProductSupplier();
        productSupplier.setProduct(product);
        productSupplier.setSupplier(supplier);
        productSupplier.setPurchasePrice(new BigDecimal("45.00"));

        when(productSupplierService.getAllProductSuppliers())
                .thenReturn(List.of(productSupplier));

        mockMvc.perform(get("/product-suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].product.productId").value(2))
                .andExpect(jsonPath("$[0].supplier.supplierId").value(2))
                .andExpect(jsonPath("$[0].purchasePrice").value(45.00));
    }



    @Test
    void getProductSupplierReturnsProductSupplier() throws Exception {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");

        ProductSupplier productSupplier = new ProductSupplier();
        productSupplier.setProduct(product);
        productSupplier.setSupplier(supplier);
        productSupplier.setPurchasePrice(new BigDecimal("45.00"));

        when(productSupplierService.getProductSupplier(any(ProductSupplierId.class)))
                .thenReturn(productSupplier);

        mockMvc.perform(get("/product-suppliers/2/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product.productId").value(2))
                .andExpect(jsonPath("$.supplier.supplierId").value(2))
                .andExpect(jsonPath("$.purchasePrice").value(45.00));
    }


    @Test
    void updateProductSupplierReturnsUpdatedProductSupplier() throws Exception {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");

        ProductSupplier updatedProductSupplier = new ProductSupplier();
        updatedProductSupplier.setProduct(product);
        updatedProductSupplier.setSupplier(supplier);
        updatedProductSupplier.setPurchasePrice(new BigDecimal("42.00"));

        when(productSupplierService.updateProductSupplier(
                any(ProductSupplierId.class),
                any(ProductSupplier.class)))
                .thenReturn(updatedProductSupplier);

        mockMvc.perform(put("/product-suppliers/2/2")
                        .contentType(APPLICATION_JSON)
                        .content("""
                        {
                            "purchasePrice": 42.00
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product.productId").value(2))
                .andExpect(jsonPath("$.supplier.supplierId").value(2))
                .andExpect(jsonPath("$.purchasePrice").value(42.00));
    }



    @Test
    void deleteProductSupplierCallsService() throws Exception {

        doNothing().when(productSupplierService)
                .deleteProductSupplier(any(ProductSupplierId.class));

        mockMvc.perform(delete("/product-suppliers/2/2"))
                .andExpect(status().isOk());

        verify(productSupplierService)
                .deleteProductSupplier(any(ProductSupplierId.class));
    }




}

