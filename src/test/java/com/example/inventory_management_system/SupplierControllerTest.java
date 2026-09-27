package com.example.inventory_management_system;

import com.example.inventory_management_system.controller.SupplierController;
import com.example.inventory_management_system.model.Supplier;
import com.example.inventory_management_system.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;




public class SupplierControllerTest {

    private final SupplierService supplierService = mock(SupplierService.class);

    private final SupplierController supplierController =
            new SupplierController(supplierService);

    private final MockMvc mockMvc =
            MockMvcBuilders.standaloneSetup(supplierController).build();

    @Test
    void getAllSuppliersReturnsSuppliers() throws Exception {

        Supplier supplier1 = new Supplier();
        supplier1.setSupplierId(1L);
        supplier1.setName("ABC Electronics");
        supplier1.setContactName("Maria Lopez");
        supplier1.setEmail("maria@abcelectronics.com");
        supplier1.setPhone("555-123-4567");

        Supplier supplier2 = new Supplier();
        supplier2.setSupplierId(2L);
        supplier2.setName("TechSource Distributors");
        supplier2.setContactName("Carlos Martinez");
        supplier2.setEmail("carlos@techsource.com");
        supplier2.setPhone("555-111-2222");

        when(supplierService.getAllSuppliers())
                .thenReturn(List.of(supplier1, supplier2));

        mockMvc.perform(get("/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].supplierId").value(1))
                .andExpect(jsonPath("$[0].name").value("ABC Electronics"))
                .andExpect(jsonPath("$[1].supplierId").value(2))
                .andExpect(jsonPath("$[1].name").value("TechSource Distributors"));
    }



    @Test
    void getSupplierByIdReturnsSupplier() throws Exception {

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");
        supplier.setContactName("Carlos Martinez");
        supplier.setEmail("carlos@techsource.com");
        supplier.setPhone("555-111-2222");

        when(supplierService.getSupplierById(2L))
                .thenReturn(supplier);

        mockMvc.perform(get("/suppliers/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supplierId").value(2))
                .andExpect(jsonPath("$.name").value("TechSource Distributors"))
                .andExpect(jsonPath("$.contactName").value("Carlos Martinez"))
                .andExpect(jsonPath("$.email").value("carlos@techsource.com"))
                .andExpect(jsonPath("$.phone").value("555-111-2222"));
    }


    @Test
    void createSupplierReturnsCreatedSupplier() throws Exception {

        Supplier supplier = new Supplier();
        supplier.setSupplierId(3L);
        supplier.setName("New Supplier");
        supplier.setContactName("John Smith");
        supplier.setEmail("john@newsupplier.com");
        supplier.setPhone("555-333-4444");

        when(supplierService.createSupplier(any(Supplier.class)))
                .thenReturn(supplier);

        mockMvc.perform(post("/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "New Supplier",
                                "contactName": "John Smith",
                                "email": "john@newsupplier.com",
                                "phone": "555-333-4444"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supplierId").value(3))
                .andExpect(jsonPath("$.name").value("New Supplier"))
                .andExpect(jsonPath("$.contactName").value("John Smith"))
                .andExpect(jsonPath("$.email").value("john@newsupplier.com"))
                .andExpect(jsonPath("$.phone").value("555-333-4444"));
    }



    @Test
    void updateSupplierReturnsUpdatedSupplier() throws Exception {

        Supplier updatedSupplier = new Supplier();
        updatedSupplier.setSupplierId(2L);
        updatedSupplier.setName("Updated TechSource");
        updatedSupplier.setContactName("Carlos Martinez");
        updatedSupplier.setEmail("updated@techsource.com");
        updatedSupplier.setPhone("555-999-8888");

        when(supplierService.updateSupplier(anyLong(), any(Supplier.class)))
                .thenReturn(updatedSupplier);

        mockMvc.perform(put("/suppliers/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Updated TechSource",
                                "contactName": "Carlos Martinez",
                                "email": "updated@techsource.com",
                                "phone": "555-999-8888"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supplierId").value(2))
                .andExpect(jsonPath("$.name").value("Updated TechSource"))
                .andExpect(jsonPath("$.contactName").value("Carlos Martinez"))
                .andExpect(jsonPath("$.email").value("updated@techsource.com"))
                .andExpect(jsonPath("$.phone").value("555-999-8888"));
    }



    @Test
    void deleteSupplierCallsService() throws Exception {

        doNothing().when(supplierService).deleteSupplier(2L);

        mockMvc.perform(delete("/suppliers/2"))
                .andExpect(status().isOk());

        verify(supplierService).deleteSupplier(2L);
    }




}


