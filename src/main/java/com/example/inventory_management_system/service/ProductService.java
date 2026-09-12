package com.example.inventory_management_system.service;

import com.example.inventory_management_system.exception.ProductNotFoundException;
import com.example.inventory_management_system.model.Product;
import com.example.inventory_management_system.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }



    public List<Product> getAllProducts(String search) {

        if (search == null || search.isBlank()) {
            return productRepository.findAll();
        }

        return productRepository.findByNameContainingIgnoreCase(search);
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


    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

}

