package com.example.inventory_management_system.model;

import java.io.Serializable;
import java.util.Objects;

public class ProductSupplierId implements Serializable {

    private Long product;

    private Long supplier;

    public ProductSupplierId() {
    }

    public ProductSupplierId(Long productId, Long supplierId) {
        this.product = productId;
        this.supplier = supplierId;
    }

    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public Long getSupplier() {
        return supplier;
    }

    public void setSupplier(Long supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductSupplierId)) return false;
        ProductSupplierId that = (ProductSupplierId) o;
        return Objects.equals(product, that.product)
                && Objects.equals(supplier, that.supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, supplier);
    }
}


