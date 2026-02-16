package com.insurance.auth_service.service;

import com.insurance.auth_service.entity.ChatMessage;
import com.insurance.auth_service.entity.User;
import com.insurance.auth_service.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatbotService {
    
    @Value("${chatbot.api.key:demo-key}")
    private String apiKey;
    
    @Value("${chatbot.api.provider:local}")  // "openai", "claude", "local"
    private String apiProvider;
    
    private final ChatMessageRepository chatMessageRepository;
    
    public ChatbotService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }
    
    /**
     * Main method to get chatbot response
     */
    public ChatMessage getChatResponse(User user, String userMessage) {
        // Save user message
        ChatMessage userMsg = new ChatMessage();
        userMsg.setUser(user);
        userMsg.setMessage(userMessage);
        userMsg.setRole("USER");
        userMsg.setMessageType("TEXT");
        chatMessageRepository.save(userMsg);
        
        // Get AI response
        String botResponse = generateResponse(userMessage, user);
        
        // Save bot response
        ChatMessage botMsg = new ChatMessage();
        botMsg.setUser(user);
        botMsg.setMessage(botResponse);
        botMsg.setRole("BOT");
        botMsg.setMessageType("TEXT");
        chatMessageRepository.save(botMsg);
        
        return botMsg;
    }
    
    /**
     * Generate response based on user message
     */
    private String generateResponse(String userMessage, User user) {
        String lower = userMessage.toLowerCase();
        
        // === HEALTH INSURANCE ===
        if (lower.contains("health") || lower.contains("medical") || lower.contains("hospital")) {
            return generateHealthInsuranceResponse(lower);
        }
        
        // === LIFE INSURANCE ===
        if (lower.contains("life") || lower.contains("term") || lower.contains("family")) {
            return generateLifeInsuranceResponse(lower);
        }
        
        // === VEHICLE INSURANCE ===
        if (lower.contains("car") || lower.contains("bike") || lower.contains("vehicle") || lower.contains("motor")) {
            return generateVehicleInsuranceResponse(lower);
        }
        
        // === PRICING ===
        if (lower.contains("price") || lower.contains("cost") || lower.contains("premium") || lower.contains("rupee")) {
            return generatePricingResponse(lower);
        }
        
        // === CLAIMS ===
        if (lower.contains("claim") || lower.contains("reimbursement") || lower.contains("settlement")) {
            return generateClaimsResponse(lower);
        }
        
        // === AGENTS ===
        if (lower.contains("agent") || lower.contains("expert") || lower.contains("consultant")) {
            return generateAgentResponse(lower);
        }
        
        // === BOOKING ===
        if (lower.contains("book") || lower.contains("appointment") || lower.contains("appointment")) {
            return generateBookingResponse(lower);
        }
        
        // === GENERAL GREETING ===
        if (lower.contains("hello") || lower.contains("hi") || lower.contains("namaste") || lower.contains("hey")) {
            return generateGreetingResponse(user);
        }
        
        // === DEFAULT ===
        return getDefaultResponse();
    }
    
    private String generateHealthInsuranceResponse(String message) {
        if (message.contains("price") || message.contains("cost")) {
            return "Our Health Insurance plans start from ₹500/month with coverage up to 5 Lakh. Family plans available with special discounts for group coverage.";
        }
        if (message.contains("coverage") || message.contains("cover")) {
            return "Our Health Plans include:\n• Cashless hospitalization in 500+ hospitals\n• Maternity & newborn care\n• Critical illness cover\n• Annual health checkups\n• Medicine & prescription coverage";
        }
        return "I can help you with our comprehensive Health Insurance plans! Ask about coverage, pricing, benefits, or book a consultation with our expert.";
    }
    
    private String generateLifeInsuranceResponse(String message) {
        if (message.contains("price") || message.contains("cost")) {
            return "Term Life Insurance starts from ₹399/month for 1 Crore coverage. Whole Life plans also available for long-term security.";
        }
        if (message.contains("family") || message.contains("security")) {
            return "Life Insurance ensures your family's financial security:\n• Death benefit payout\n• Loan against policy\n• Tax benefits (Section 80C)\n• Retirement planning\n• Children's education fund";
        }
        return "Life Insurance protects your family's future! Get quotes, compare plans, or chat with our agent today.";
    }
    
    private String generateVehicleInsuranceResponse(String message) {
        if (message.contains("bike") || message.contains("two-wheeler")) {
            return "Bike Insurance from ₹800/year:\n• Third-party liability coverage\n• Own damage protection\n• No depreciation claims\n• 24x7 roadside assistance\n• Quick claim settlement";
        }
        if (message.contains("car")) {
            return "Car Insurance from ₹2,500/year:\n• Comprehensive coverage\n• Engine protection\n• Zero depreciation\n• Cashless repair network\n• Personal accident cover";
        }
        return "Protect your vehicle with comprehensive insurance! I can help with bike, car, or commercial vehicle coverage.";
    }
    
    private String generatePricingResponse(String message) {
        return "Our premium rates are competitive:\n\n📊 Health Insurance: ₹500 - ₹2,000/month\n💰 Life Insurance: ₹399 - ₹1,500/month\n🚗 Vehicle Insurance: ₹800 - ₹5,000/year\n\nRates depend on age, health, coverage amount, and city. Book a free consultation to get a personalized quote!";
    }
    
    private String generateClaimsResponse(String message) {
        return "Filing a claim is simple:\n\n1. Notify us within 30 days\n2. Submit required documents\n3. Our team verifies (usually 3-7 days)\n4. Claim processed & settled\n\nWe have 99%+ claim settlement ratio. Upload documents on our portal or visit nearest branch.";
    }
    
    private String generateAgentResponse(String message) {
        return "Connect with our top insurance experts:\n\n✨ Dr. Rajesh Sharma - Health Insurance Specialist\n✨ Priya Patel - Life Insurance Expert\n✨ Vikram Singh - Vehicle Insurance Consultant\n\nBook a free consultation by clicking 'Book Appointment' or I can connect you instantly!";
    }
    
    private String generateBookingResponse(String message) {
        return "Booking an appointment is easy!\n\n1. Click 'Book Appointment' tab\n2. Choose your preferred agent\n3. Select date & time\n4. Describe your requirement\n5. Confirm & connect\n\nAvailable 9 AM - 9 PM, 7 days a week.";
    }
    
    private String generateGreetingResponse(User user) {
        String name = (user != null && user.getFullName() != null) ? user.getFullName() : "User";
        return "Namaste " + name + "! 🙏\n\nWelcome to InsurAI Pro - Your trusted insurance partner. I'm here to help with:\n\n📋 Health, Life & Vehicle Insurance\n🎯 Find best plans matching your needs\n💬 Answer insurance queries\n👥 Connect with expert agents\n\nHow can I assist you today?";
    }
    
    private String getDefaultResponse() {
        return "I'm here to help! You can ask me about:\n\n💬 Health Insurance\n💬 Life Insurance\n💬 Vehicle Insurance\n💬 Premium pricing\n💬 Claim process\n💬 Book expert consultation\n\nWhat would you like to know?";
    }
    
    /**
     * Get chat history for user
     */
    public List<ChatMessage> getChatHistory(User user, int limit) {
        return chatMessageRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    /**
     * Clear old chat messages (older than 30 days)
     */
    public void cleanupOldMessages(int daysOld) {
        // Implementation depends on your requirements
        // chatMessageRepository.deleteOlderThan(LocalDateTime.now().minusDays(daysOld));
    }
}