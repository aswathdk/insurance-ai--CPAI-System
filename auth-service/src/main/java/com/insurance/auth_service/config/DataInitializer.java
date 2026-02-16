package com.insurance.auth_service.config;

import com.insurance.auth_service.entity.User;
import com.insurance.auth_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if admin exists. If NOT, create one.
            if (!userRepo.existsByEmail("admin@insurance.com")) {
                
                User admin = new User();
                admin.setFullName("System Administrator");
                admin.setEmail("admin@insurance.com");
                
                // --- SETTING PASSWORD TO admin123 ---
                admin.setPassword(passwordEncoder.encode("admin123")); 
                
                admin.setRole("ADMIN");
                admin.setApproved(true); // Auto-approve
                admin.setPhoneNumber("0000000000");
                admin.setCity("Headquarters");
                admin.setAddress("Admin Office");
                admin.setZipCode("00000");
                
                userRepo.save(admin);
                System.out.println(">>> SUCCESS: Created Admin User (admin@insurance.com / admin123)");
            }
        };
    }
}