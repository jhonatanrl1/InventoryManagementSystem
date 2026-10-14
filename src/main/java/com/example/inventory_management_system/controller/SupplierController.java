package com.example.inventory_management_system.controller;

import com.example.inventory_management_system.model.Supplier;
import com.example.inventory_management_system.service.SupplierService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;



@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }



    @GetMapping
    public List<Supplier> getAllSuppliers(
            @RequestParam(required = false) String search) {
        return supplierService.getAllSuppliers(search);
    }




    @GetMapping("/{id}")
    public Supplier getSupplierById(@PathVariable Long id) {
        return supplierService.getSupplierById(id);
    }

    @PostMapping
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        return supplierService.createSupplier(supplier);
    }

    @PutMapping("/{id}")
    public Supplier updateSupplier(
            @PathVariable Long id,
            @RequestBody Supplier supplierDetails) {

        return supplierService.updateSupplier(id, supplierDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
    }
}


