package com.insurance.auth_service.repository;
import com.insurance.auth_service.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByCustomerId(Long customerId);
    List<Appointment> findBySlotAgentId(Long agentId);
}