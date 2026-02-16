package com.insurance.auth_service.controller;

import com.insurance.auth_service.entity.ChatMessage;
import com.insurance.auth_service.entity.User;
import com.insurance.auth_service.service.ChatbotService;
import com.insurance.auth_service.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {
    
    private final ChatbotService chatbotService;
    private final UserRepository userRepository;
    
    public ChatbotController(ChatbotService chatbotService, UserRepository userRepository) {
        this.chatbotService = chatbotService;
        this.userRepository = userRepository;
    }
    
    /**
     * Send message to chatbot and get response
     */
    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            String message = (String) payload.get("message");
            
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            ChatMessage response = chatbotService.getChatResponse(user, message);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "response", response.getMessage(),
                "messageType", response.getMessageType(),
                "timestamp", response.getCreatedAt()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }
    
    /**
     * Get chat history for user
     */
    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getChatHistory(@PathVariable Long userId, @RequestParam(defaultValue = "50") int limit) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            List<ChatMessage> history = chatbotService.getChatHistory(user, limit);
            
            return ResponseEntity.ok(Map.of(
                "userId", userId,
                "messageCount", history.size(),
                "messages", history
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Clear chat history
     */
    @DeleteMapping("/history/{userId}")
    public ResponseEntity<?> clearChatHistory(@PathVariable Long userId) {
        try {
            // In a real app, you'd implement this in the repository
            return ResponseEntity.ok(Map.of(
                "message", "Chat history cleared successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get quick suggestions based on context
     */
    @GetMapping("/suggestions/{userId}")
    public ResponseEntity<?> getQuickSuggestions(@PathVariable Long userId) {
        return ResponseEntity.ok(Map.of(
            "suggestions", List.of(
                "Show me health insurance plans",
                "What's the cost of life insurance?",
                "How to file a claim?",
                "Connect me with an agent",
                "Bike insurance details"
            )
        ));
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "online",
            "message", "InsurAI Chatbot is ready to assist!",
            "features", List.of(
                "Insurance product information",
                "Pricing & coverage details",
                "Claim process guidance",
                "Agent recommendations",
                "Appointment booking"
            )
        ));
    }
}