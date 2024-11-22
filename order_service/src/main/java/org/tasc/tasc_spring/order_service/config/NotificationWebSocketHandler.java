package org.tasc.tasc_spring.order_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.tasc.tasc_spring.order_service.service.CartService;

import java.util.concurrent.ConcurrentHashMap;


public class NotificationWebSocketHandler implements WebSocketHandler {
    @Autowired
    private  CartService cartService;
    public static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = null ;

        if (session.getUri() != null && session.getUri().getQuery() != null) {
            String[] queryParams = session.getUri().getQuery().split("&");
            for (String param : queryParams) {
                if (param.startsWith("token=")) {
                    token = param.split("=")[1];
                    break;
                }
            }
        }

        if (token == null || token.isEmpty()) {
            session.close(CloseStatus.BAD_DATA);
            System.err.println("Connection rejected: Token is missing or invalid");
            return;
        }
        System.out.println("New WebSocket connection established with token: " + token);
        sessions.put(token, session);

        cartService.sendNotification(token);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = message.getPayload().toString();
        System.out.println("Received message: " + payload);

        session.sendMessage(new TextMessage("Message received!"));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception){
        System.err.println("WebSocket error: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String token = null;

        // Lấy token từ query string
        if (session.getUri() != null && session.getUri().getQuery() != null) {
            String[] queryParams = session.getUri().getQuery().split("&");
            for (String param : queryParams) {
                if (param.startsWith("token=")) {
                    token = param.split("=")[1];
                    break;
                }
            }
        }

        if (token != null) {
            sessions.remove(token);
            System.out.println("WebSocket connection closed for token: " + token);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


}
