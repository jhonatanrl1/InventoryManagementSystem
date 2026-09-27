package com.example.inventory_management_system;

import com.example.inventory_management_system.controller.ProductController;
import com.example.inventory_management_system.model.Product;
import com.example.inventory_management_system.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


public class ProductControllerTest {

    private final ProductService productService =
            mock(ProductService.class);

    private final ProductController productController =
            new ProductController(productService);

    private final MockMvc mockMvc =
            MockMvcBuilders.standaloneSetup(productController).build();



    @Test
    void getAllProductsReturnsProducts() throws Exception {

        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Laptop");
        product1.setSellingPrice(new BigDecimal("999.99"));
        product1.setQuantityInStock(5);
        product1.setLowStockThreshold(2);

        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setName("Keyboard");
        product2.setSellingPrice(new BigDecimal("79.99"));
        product2.setQuantityInStock(10);
        product2.setLowStockThreshold(3);

        when(productService.getAllProducts(null))
                .thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].name").value("Keyboard"));
    }

    @Test
    void getProductByIdReturnsProduct() throws Exception {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");
        product.setSellingPrice(new BigDecimal("79.99"));
        product.setQuantityInStock(10);
        product.setLowStockThreshold(3);

        when(productService.getProductById(2L))
                .thenReturn(product);

        mockMvc.perform(get("/products/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(2))
                .andExpect(jsonPath("$.name").value("Keyboard"))
                .andExpect(jsonPath("$.sellingPrice").value(79.99))
                .andExpect(jsonPath("$.quantityInStock").value(10))
                .andExpect(jsonPath("$.lowStockThreshold").value(3));
    }



    @Test
    void createProductReturnsCreatedProduct() throws Exception {

        Product product = new Product();
        product.setProductId(3L);
        product.setName("Monitor");
        product.setSellingPrice(new BigDecimal("199.99"));
        product.setQuantityInStock(5);
        product.setLowStockThreshold(2);

        when(productService.createProduct(any(Product.class)))
                .thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Monitor",
                                "description": "24 inch monitor",
                                "sellingPrice": 199.99,
                                "quantityInStock": 5,
                                "lowStockThreshold": 2
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(3))
                .andExpect(jsonPath("$.name").value("Monitor"))
                .andExpect(jsonPath("$.sellingPrice").value(199.99))
                .andExpect(jsonPath("$.quantityInStock").value(5))
                .andExpect(jsonPath("$.lowStockThreshold").value(2));
    }


    @Test
    void updateProductReturnsUpdatedProduct() throws Exception {

        Product updatedProduct = new Product();
        updatedProduct.setProductId(2L);
        updatedProduct.setName("Updated Keyboard");
        updatedProduct.setSellingPrice(new BigDecimal("89.99"));
        updatedProduct.setQuantityInStock(15);
        updatedProduct.setLowStockThreshold(4);

        when(productService.updateProduct(anyLong(), any(Product.class)))
                .thenReturn(updatedProduct);

        mockMvc.perform(put("/products/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Updated Keyboard",
                                "description": "Updated mechanical keyboard",
                                "sellingPrice": 89.99,
                                "quantityInStock": 15,
                                "lowStockThreshold": 4
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(2))
                .andExpect(jsonPath("$.name").value("Updated Keyboard"))
                .andExpect(jsonPath("$.sellingPrice").value(89.99))
                .andExpect(jsonPath("$.quantityInStock").value(15))
                .andExpect(jsonPath("$.lowStockThreshold").value(4));
    }


    @Test
    void deleteProductReturnsOk() throws Exception {

        doNothing().when(productService).deleteProduct(2L);

        mockMvc.perform(delete("/products/2"))
                .andExpect(status().isOk());

        verify(productService).deleteProduct(2L);
    }



    @Test
    void getAllProductsReturnsMatchingProductsForSearch() throws Exception {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");
        product.setSellingPrice(new BigDecimal("79.99"));
        product.setQuantityInStock(10);
        product.setLowStockThreshold(3);

        when(productService.getAllProducts("keyboard"))
                .thenReturn(List.of(product));

        mockMvc.perform(get("/products")
                        .param("search", "keyboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productId").value(2))
                .andExpect(jsonPath("$[0].name").value("Keyboard"));
    }



    @Test
    void getAllProductsReturnsLowStockProducts() throws Exception {

        Product product = new Product();
        product.setProductId(2L);
        product.setName("Keyboard");
        product.setSellingPrice(new BigDecimal("79.99"));
        product.setQuantityInStock(2);
        product.setLowStockThreshold(3);

        when(productService.getLowStockProducts())
                .thenReturn(List.of(product));

        mockMvc.perform(get("/products")
                        .param("lowStock", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].productId").value(2))
                .andExpect(jsonPath("$[0].name").value("Keyboard"))
                .andExpect(jsonPath("$[0].quantityInStock").value(2))
                .andExpect(jsonPath("$[0].lowStockThreshold").value(3));
    }



}

