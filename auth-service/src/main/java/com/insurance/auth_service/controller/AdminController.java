package com.insurance.auth_service.controller;

import com.insurance.auth_service.entity.User;
import com.insurance.auth_service.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepo;

    public AdminController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // 1. Get Pending Agents (For Approval)
    @GetMapping("/pending-agents")
    public List<User> getPendingAgents() {
        return userRepo.findAll().stream()
                .filter(u -> "AGENT".equals(u.getRole()) && !u.isApproved())
                .collect(Collectors.toList());
    }

    // 2. Approve Agent
    @PutMapping("/approve-agent/{id}")
    public ResponseEntity<?> approveAgent(@PathVariable Long id) {
        User agent = userRepo.findById(id).orElseThrow();
        agent.setApproved(true);
        userRepo.save(agent);
        return ResponseEntity.ok(Map.of("message", "Agent Approved Successfully"));
    }

    // 3. NEW: Generic Method to get users by Role (AGENT or CUSTOMER)
    @GetMapping("/users/{role}")
    public List<User> getUsersByRole(@PathVariable String role) {
        return userRepo.findAll().stream()
                .filter(u -> u.getRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }
} 