package com.example.inventory_management_system.controller;


import com.example.inventory_management_system.model.Product;
import com.example.inventory_management_system.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

}
