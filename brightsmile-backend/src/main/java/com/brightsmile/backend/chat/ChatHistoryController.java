package com.brightsmile.backend.chat;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatHistoryController {

    private final ChatHistoryService chatHistoryService;

    public ChatHistoryController(ChatHistoryService chatHistoryService) {
        this.chatHistoryService = chatHistoryService;
    }

    @DeleteMapping("/historial/{sessionId}")
    public void limpiarHistorial(@PathVariable String sessionId) {
        chatHistoryService.limpiarHistorial(sessionId);
    }
}