package com.insurance.auth_service.repository;



import com.insurance.auth_service.entity.ChatMessage;
import com.insurance.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


// ===== CHAT MESSAGE REPOSITORY =====
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByUserOrderByCreatedAtDesc(User user);
    List<ChatMessage> findByUserAndRoleOrderByCreatedAtDesc(User user, String role);
    Long countByUserAndIsResolvedFalse(User user);
}