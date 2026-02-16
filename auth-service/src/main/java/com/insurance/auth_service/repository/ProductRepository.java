package com.insurance.auth_service.repository;



import com.insurance.auth_service.entity.InsuranceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// ===== INSURANCE PRODUCT REPOSITORY =====
public interface ProductRepository extends JpaRepository<InsuranceProduct, Long> {
    List<InsuranceProduct> findByTypeAndIsActiveTrue(String type);
    Long countByTypeAndIsActiveTrue(String type);
    List<InsuranceProduct> findByIsActiveTrue();
}