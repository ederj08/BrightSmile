package com.brightsmile.backend.chat;

// Clase que representa un mensaje en el chat
// Viaja entre Angular y Spring Boot via WebSocket
public class ChatMessage {

    // Quién manda el mensaje: "USER" o "BOT"
    private String sender;

    // El contenido del mensaje
    private String content;

    // Timestamp del mensaje en milisegundos
    private long timestamp;

    // Si el bot está "escribiendo" — para mostrar el indicador de typing
    private boolean typing;

    public ChatMessage() {}

    // Constructor rápido para crear mensajes del bot
    public ChatMessage(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
        this.typing = false;
    }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public boolean isTyping() { return typing; }
    public void setTyping(boolean typing) { this.typing = typing; }
}