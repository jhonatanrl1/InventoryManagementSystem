package com.example.inventory_management_system;

import com.example.inventory_management_system.model.Supplier;
import com.example.inventory_management_system.repository.SupplierRepository;
import com.example.inventory_management_system.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.example.inventory_management_system.exception.SupplierNotFoundException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    @Test
    void getSupplierByIdReturnsSupplier() {

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");
        supplier.setContactName("Carlos Martinez");
        supplier.setEmail("carlos@techsource.com");
        supplier.setPhone("555-111-2222");

        when(supplierRepository.findById(2L))
                .thenReturn(Optional.of(supplier));

        Supplier result = supplierService.getSupplierById(2L);

        assertEquals(2L, result.getSupplierId());
        assertEquals("TechSource Distributors", result.getName());
        assertEquals("Carlos Martinez", result.getContactName());
    }

    @Test
    void getSupplierByIdThrowsExceptionWhenSupplierDoesNotExist() {

        when(supplierRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                SupplierNotFoundException.class,
                () -> supplierService.getSupplierById(999L)
        );
    }


    @Test
    void createSupplierReturnsSavedSupplier() {

        Supplier supplier = new Supplier();
        supplier.setSupplierId(3L);
        supplier.setName("Office Supplies Inc.");
        supplier.setContactName("David Brown");
        supplier.setEmail("david@officesupplies.com");
        supplier.setPhone("555-333-4444");

        when(supplierRepository.save(supplier))
                .thenReturn(supplier);

        Supplier result = supplierService.createSupplier(supplier);

        assertEquals(3L, result.getSupplierId());
        assertEquals("Office Supplies Inc.", result.getName());
        assertEquals("David Brown", result.getContactName());
    }

    @Test
    void updateSupplierReturnsUpdatedSupplier() {

        Supplier existingSupplier = new Supplier();
        existingSupplier.setSupplierId(2L);
        existingSupplier.setName("TechSource Distributors");
        existingSupplier.setContactName("Carlos Martinez");
        existingSupplier.setEmail("carlos@techsource.com");
        existingSupplier.setPhone("555-111-2222");

        Supplier supplierDetails = new Supplier();
        supplierDetails.setName("Updated TechSource");
        supplierDetails.setContactName("John Smith");
        supplierDetails.setEmail("john@techsource.com");
        supplierDetails.setPhone("555-999-8888");

        when(supplierRepository.findById(2L))
                .thenReturn(Optional.of(existingSupplier));

        when(supplierRepository.save(existingSupplier))
                .thenReturn(existingSupplier);

        Supplier result = supplierService.updateSupplier(2L, supplierDetails);

        assertEquals(2L, result.getSupplierId());
        assertEquals("Updated TechSource", result.getName());
        assertEquals("John Smith", result.getContactName());
        assertEquals("john@techsource.com", result.getEmail());
        assertEquals("555-999-8888", result.getPhone());
    }

    @Test
    void getAllSuppliersReturnsAllSuppliers() {

        Supplier supplier1 = new Supplier();
        supplier1.setSupplierId(1L);
        supplier1.setName("ABC Electronics");

        Supplier supplier2 = new Supplier();
        supplier2.setSupplierId(2L);
        supplier2.setName("TechSource Distributors");

        when(supplierRepository.findAll())
                .thenReturn(List.of(supplier1, supplier2));

        List<Supplier> result = supplierService.getAllSuppliers();

        assertEquals(2, result.size());
        assertEquals("ABC Electronics", result.get(0).getName());
        assertEquals("TechSource Distributors", result.get(1).getName());
    }


    @Test
    void deleteSupplierRemovesSupplier() {

        Supplier supplier = new Supplier();
        supplier.setSupplierId(2L);
        supplier.setName("TechSource Distributors");

        when(supplierRepository.findById(2L))
                .thenReturn(Optional.of(supplier));

        supplierService.deleteSupplier(2L);

        verify(supplierRepository).delete(supplier);
    }



}

