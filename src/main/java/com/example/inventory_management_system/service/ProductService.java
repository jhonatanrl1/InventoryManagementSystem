package com.example.inventory_management_system.service;

import com.example.inventory_management_system.exception.ProductNotFoundException;
import com.example.inventory_management_system.model.Product;
import com.example.inventory_management_system.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getAllProducts(String search, boolean lowStock, Pageable pageable) {

        boolean hasSearch = search != null && !search.isBlank();

        if (hasSearch && lowStock) {
            return productRepository.searchLowStockProducts(search.trim(), pageable);
        }

        if (hasSearch) {
            return productRepository.searchProducts(search.trim(), pageable);
        }

        if (lowStock) {
            return productRepository.findLowStockProducts(pageable);
        }

        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setSellingPrice(product.getSellingPrice());
        existingProduct.setQuantityInStock(product.getQuantityInStock());
        existingProduct.setLowStockThreshold(product.getLowStockThreshold());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}

