package com.brightsmile.backend.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    private final OpenAIService openAIService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatHistoryService chatHistoryService;

    public ChatController(OpenAIService openAIService,
                          SimpMessagingTemplate messagingTemplate,
                          ChatHistoryService chatHistoryService) {
        this.openAIService = openAIService;
        this.messagingTemplate = messagingTemplate;
        this.chatHistoryService = chatHistoryService;
    }

    @MessageMapping("/chat.send")
    public void handleMessage(ChatMessage message) {

        // sessionId real: viene del campo "sender" que Angular llena con su ID único
        String sessionId = message.getSender();
        List<Map<String, String>> history = chatHistoryService.obtenerHistorial(sessionId);

        // Indicador de "escribiendo..."
        ChatMessage typingMessage = new ChatMessage("BOT", "");
        typingMessage.setTyping(true);
        messagingTemplate.convertAndSend("/topic/chat/" + sessionId, typingMessage);

        String botResponse = openAIService.chat(message.getContent(), history);

        Map<String, String> userEntry = new HashMap<>();
        userEntry.put("role", "user");
        userEntry.put("content", message.getContent());
        history.add(userEntry);

        Map<String, String> botEntry = new HashMap<>();
        botEntry.put("role", "assistant");
        botEntry.put("content", botResponse);
        history.add(botEntry);

        if (history.size() > 20) {
            history.subList(0, history.size() - 20).clear();
        }

        ChatMessage botMessage = new ChatMessage("BOT", botResponse);
        messagingTemplate.convertAndSend("/topic/chat/" + sessionId, botMessage);
    }
}