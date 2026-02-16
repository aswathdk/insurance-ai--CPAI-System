package com.insurance.auth_service.controller;

import com.insurance.auth_service.entity.*;
import com.insurance.auth_service.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agent")
public class AgentController {
    
    private final AgentAvailabilityRepository availabilityRepo;
    private final UserRepository userRepo;

    public AgentController(AgentAvailabilityRepository a, UserRepository u) {
        this.availabilityRepo = a; 
        this.userRepo = u;
    }

    @PostMapping("/availability")
    public ResponseEntity<?> addAvailability(@RequestBody AgentAvailability slot, @RequestParam String email) {
        User agent = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Agent not found"));
        slot.setAgent(agent);
        availabilityRepo.save(slot);
        return ResponseEntity.ok(Map.of("message", "Slot added"));
    }

    @GetMapping("/my-slots")
    public ResponseEntity<?> getSlotsByEmail(@RequestParam String email) {
        User agent = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Agent not found"));
        return ResponseEntity.ok(availabilityRepo.findByAgentId(agent.getId()));
    }

    @DeleteMapping("/availability/{id}")
    public ResponseEntity<?> deleteAvailability(@PathVariable Long id) {
        availabilityRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Deleted"));
    }

    // UPDATED METHOD: Filters by both 'Not Booked' AND 'Agent Approved'
    @GetMapping("/available-slots")
    public List<AgentAvailability> getAllAvailableSlots() {
        return availabilityRepo.findAll().stream()
                .filter(s -> !s.isBooked())                  // Only show open slots
                .filter(s -> s.getAgent().isApproved())      // Only show APPROVED agents
                .collect(Collectors.toList());
    }
}