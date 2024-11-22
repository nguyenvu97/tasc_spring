package org.tasc.tasc_spring.order_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.jmx.support.NotificationListenerHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(notificationWebSocketHandler(), "/ws/notification")
                .setAllowedOriginPatterns("*");
    }
    @Bean
    public NotificationWebSocketHandler notificationWebSocketHandler() {
        return new NotificationWebSocketHandler();
    }
}
