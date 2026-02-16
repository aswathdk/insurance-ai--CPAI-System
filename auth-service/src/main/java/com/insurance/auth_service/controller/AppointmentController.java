package com.insurance.auth_service.controller;

import com.insurance.auth_service.entity.*;
import com.insurance.auth_service.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    
    private final AppointmentRepository apptRepo;
    private final AgentAvailabilityRepository slotRepo;
    private final UserRepository userRepo;

    public AppointmentController(AppointmentRepository a, AgentAvailabilityRepository s, UserRepository u) {
        this.apptRepo = a; 
        this.slotRepo = s; 
        this.userRepo = u;
    }

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestBody Map<String, Object> payload) {
        Long slotId = Long.valueOf(payload.get("slotId").toString());
        Long customerId = Long.valueOf(payload.get("customerId").toString());
        String description = (String) payload.get("description");

        AgentAvailability slot = slotRepo.findById(slotId)
            .orElseThrow(() -> new RuntimeException("Slot not found"));
        
        User customer = userRepo.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Mark slot as booked
        slot.setBooked(true);
        slotRepo.save(slot);

        // Create appointment
        Appointment appt = new Appointment();
        appt.setSlot(slot);
        appt.setCustomer(customer);
        appt.setIssueDescription(description);
        appt.setStatus("CONFIRMED");
        appt.setBookedAt(LocalDateTime.now());
        apptRepo.save(appt);

        return ResponseEntity.ok(Map.of("message", "Booked successfully"));
    }

    @GetMapping("/my-appointments/{customerId}")
    public List<Appointment> getMyAppointments(@PathVariable Long customerId) {
        return apptRepo.findByCustomerId(customerId);
    }

    // --- NEW METHOD ADDED BELOW ---
    // This allows the Agent to see who booked their slots
    @GetMapping("/agent-appointments/{agentId}")
    public List<Appointment> getAgentAppointments(@PathVariable Long agentId) {
        return apptRepo.findBySlotAgentId(agentId);
    }
}