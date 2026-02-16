package com.insurance.auth_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "insurance_products")
public class InsuranceProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;                    // "Health Shield Gold"
    private String type;                    // HEALTH, LIFE, VEHICLE, HOME
    
    @Column(precision = 10, scale = 2)
    private BigDecimal basePrice;           // Monthly/Annual premium
    
    private String coverage;                // "Up to 5 Lakh"
    private String description;             // Long description
    private String shortDesc;               // 1-line description
    
    @Column(length = 1000)
    private String features;                // Comma-separated features
    
    private Integer minAge;
    private Integer maxAge;
    private Boolean isActive;               // Active/Inactive status
    
    private String imageUrl;                // Product image
    private Integer tenureMonths;           // Coverage duration
    private String claimProcess;            // How to claim
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    
    public String getCoverage() { return coverage; }
    public void setCoverage(String coverage) { this.coverage = coverage; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getShortDesc() { return shortDesc; }
    public void setShortDesc(String shortDesc) { this.shortDesc = shortDesc; }
    
    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }
    
    public Integer getMinAge() { return minAge; }
    public void setMinAge(Integer minAge) { this.minAge = minAge; }
    
    public Integer getMaxAge() { return maxAge; }
    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Integer getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }
    
    public String getClaimProcess() { return claimProcess; }
    public void setClaimProcess(String claimProcess) { this.claimProcess = claimProcess; }
}