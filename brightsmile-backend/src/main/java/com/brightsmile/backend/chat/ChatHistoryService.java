package com.brightsmile.backend.chat;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatHistoryService {

    private final ConcurrentHashMap<String, List<Map<String, String>>> conversationHistory
            = new ConcurrentHashMap<>();

    public List<Map<String, String>> obtenerHistorial(String sessionId) {
        return conversationHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());
    }

    public void limpiarHistorial(String sessionId) {
        conversationHistory.remove(sessionId);
    }
}