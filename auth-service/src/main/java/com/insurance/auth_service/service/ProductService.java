package com.insurance.auth_service.service;

import com.insurance.auth_service.entity.InsuranceProduct;
import com.insurance.auth_service.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    /**
     * Get all active products
     */
    public List<InsuranceProduct> getAllActiveProducts() {
        return productRepository.findAll().stream()
                .filter(p -> p.getIsActive() != null && p.getIsActive())
                .collect(Collectors.toList());
    }
    
    /**
     * Get products by type (HEALTH, LIFE, VEHICLE, HOME)
     */
    public List<InsuranceProduct> getProductsByType(String type) {
        return productRepository.findByTypeAndIsActiveTrue(type);
    }
    
    /**
     * Get product by ID
     */
    public Optional<InsuranceProduct> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    /**
     * Create new product (Admin only)
     */
    public InsuranceProduct createProduct(InsuranceProduct product) {
        if (product.getIsActive() == null) {
            product.setIsActive(true);
        }
        return productRepository.save(product);
    }
    
    /**
     * Update product
     */
    public InsuranceProduct updateProduct(Long id, InsuranceProduct updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setType(updatedProduct.getType());
                    product.setBasePrice(updatedProduct.getBasePrice());
                    product.setCoverage(updatedProduct.getCoverage());
                    product.setDescription(updatedProduct.getDescription());
                    product.setShortDesc(updatedProduct.getShortDesc());
                    product.setFeatures(updatedProduct.getFeatures());
                    product.setMinAge(updatedProduct.getMinAge());
                    product.setMaxAge(updatedProduct.getMaxAge());
                    product.setIsActive(updatedProduct.getIsActive());
                    product.setImageUrl(updatedProduct.getImageUrl());
                    product.setTenureMonths(updatedProduct.getTenureMonths());
                    product.setClaimProcess(updatedProduct.getClaimProcess());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    
    /**
     * Delete product (soft delete - mark inactive)
     */
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresent(product -> {
            product.setIsActive(false);
            productRepository.save(product);
        });
    }
    
    /**
     * Get products suitable for user age
     */
    public List<InsuranceProduct> getProductsForAge(Integer userAge) {
        return productRepository.findAll().stream()
                .filter(p -> p.getIsActive() && 
                            (p.getMinAge() == null || userAge >= p.getMinAge()) &&
                            (p.getMaxAge() == null || userAge <= p.getMaxAge()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get featured products
     */
    public List<InsuranceProduct> getFeaturedProducts() {
        return productRepository.findAll().stream()
                .filter(p -> p.getIsActive())
                .limit(6)
                .collect(Collectors.toList());
    }
    
    /**
     * Search products by name or description
     */
    public List<InsuranceProduct> searchProducts(String keyword) {
        String lower = keyword.toLowerCase();
        return productRepository.findAll().stream()
                .filter(p -> p.getIsActive() && (
                    p.getName().toLowerCase().contains(lower) ||
                    p.getDescription().toLowerCase().contains(lower) ||
                    p.getShortDesc().toLowerCase().contains(lower)
                ))
                .collect(Collectors.toList());
    }
    
    /**
     * Get count of products by type
     */
    public Long countProductsByType(String type) {
        return productRepository.countByTypeAndIsActiveTrue(type);
    }
}