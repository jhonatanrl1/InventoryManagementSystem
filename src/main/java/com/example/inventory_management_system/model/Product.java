package com.example.inventory_management_system.model;

import jakarta.persistence.Entity;
import java.math.BigDecimal;


@Entity
public class Product {

    //Product entity Fields
    private Long productId;
    private String name;
    private String description;
    private BigDecimal sellingPrice;
    private int quantityInStock;
    private int lowStockThreshold;







}
