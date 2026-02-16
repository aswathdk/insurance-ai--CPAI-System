package com.insurance.auth_service.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.insurance.auth_service.dto.*;
import com.insurance.auth_service.entity.User;
import com.insurance.auth_service.repository.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository u, PasswordEncoder p, AuthenticationManager a) {
        this.userRepository = u; 
        this.passwordEncoder = p; 
        this.authenticationManager = a;
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email taken");
        }
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        
        // SAVE NEW FIELDS
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setZipCode(request.getZipCode());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setSpecialization(request.getSpecialization());
        user.setExperience(request.getExperience());
        
        // --- NEW APPROVAL LOGIC ---
        if ("AGENT".equalsIgnoreCase(request.getRole())) {
            user.setApproved(false); // Agents must wait for Admin
        } else {
            user.setApproved(true);  // Customers/Admins auto-approved
        }
        
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        
        if (!user.isApproved()) {
            throw new RuntimeException("Account not approved yet.");
        }

        // PASS user.getFullName() HERE
        return new LoginResponse(user.getId(), user.getEmail(), user.getRole(), user.getFullName());
    }}
