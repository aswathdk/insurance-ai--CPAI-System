package com.insurance.auth_service.controller;

import com.insurance.auth_service.entity.InsuranceProduct;
import com.insurance.auth_service.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * Get all active products
     */
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<InsuranceProduct> products = productService.getAllActiveProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get featured products for homepage
     */
    @GetMapping("/featured")
    public ResponseEntity<?> getFeaturedProducts() {
        List<InsuranceProduct> products = productService.getFeaturedProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by type (HEALTH, LIFE, VEHICLE, HOME)
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getProductsByType(@PathVariable String type) {
        List<InsuranceProduct> products = productService.getProductsByType(type);
        return ResponseEntity.ok(Map.of(
            "type", type,
            "products", products,
            "count", products.size()
        ));
    }
    
    /**
     * Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Search products
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> searchProducts(@PathVariable String keyword) {
        List<InsuranceProduct> results = productService.searchProducts(keyword);
        return ResponseEntity.ok(Map.of(
            "keyword", keyword,
            "results", results,
            "count", results.size()
        ));
    }
    
    /**
     * Get products suitable for user age
     */
    @GetMapping("/suitable/age/{age}")
    public ResponseEntity<?> getProductsForAge(@PathVariable Integer age) {
        List<InsuranceProduct> products = productService.getProductsForAge(age);
        return ResponseEntity.ok(Map.of(
            "age", age,
            "products", products,
            "count", products.size()
        ));
    }
    
    /**
     * Get statistics on products
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getProductStats() {
        return ResponseEntity.ok(Map.of(
            "totalProducts", productService.getAllActiveProducts().size(),
            "healthProducts", productService.countProductsByType("HEALTH"),
            "lifeProducts", productService.countProductsByType("LIFE"),
            "vehicleProducts", productService.countProductsByType("VEHICLE")
        ));
    }
    
    /**
     * Create new product (Admin only)
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody InsuranceProduct product) {
        try {
            InsuranceProduct created = productService.createProduct(product);
            return ResponseEntity.status(201).body(Map.of(
                "message", "Product created successfully",
                "product", created
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Update product (Admin only)
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody InsuranceProduct product) {
        try {
            InsuranceProduct updated = productService.updateProduct(id, product);
            return ResponseEntity.ok(Map.of(
                "message", "Product updated successfully",
                "product", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Delete product (Admin only)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}