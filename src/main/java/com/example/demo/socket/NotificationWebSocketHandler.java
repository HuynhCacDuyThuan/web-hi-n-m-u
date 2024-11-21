package com.example.demo.socket;

import com.example.demo.model.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Add new WebSocket session to the list
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove WebSocket session from the list
        sessions.remove(session);
    }

    // Method to send a notification to all connected WebSocket clients
    public void sendNotification(Notification notification) {
        sessions.forEach(session -> {
            try {
                // Convert the Notification object to a JSON string
                String message = objectMapper.writeValueAsString(notification);

                // Send the message to the WebSocket session
                session.sendMessage(new TextMessage(message));

                // Debug log
                System.out.println("Sent notification: " + message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
