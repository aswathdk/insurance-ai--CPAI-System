package com.insurance.auth_service.repository;
import com.insurance.auth_service.entity.AgentAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AgentAvailabilityRepository extends JpaRepository<AgentAvailability, Long> {
    List<AgentAvailability> findByAgentId(Long agentId);
}