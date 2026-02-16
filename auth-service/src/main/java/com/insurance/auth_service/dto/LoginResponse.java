package com.insurance.auth_service.dto;

public class LoginResponse {
    private Long id;
    private String email;
    private String role;
    private String fullName; // <--- ADD THIS
    private String token;
    
    public LoginResponse(Long id, String email, String role, String fullName) { // <--- UPDATE CONSTRUCTOR
        this.id = id;
        this.email = email;
        this.role = role;
        this.fullName = fullName;
        this.token = "dummy-token";
    }

    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; } // <--- ADD GETTER
    public String getToken() { return token; }
}