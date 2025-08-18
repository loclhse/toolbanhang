package com.josh.foodorder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import jakarta.servlet.ServletContext;
import org.springframework.boot.web.servlet.ServletContextInitializer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(1024 * 1024) // 1MB message size
                   .setSendBufferSizeLimit(1024 * 1024) // 1MB buffer size
                   .setSendTimeLimit(20 * 1000) // 20 seconds
                   .setTimeToFirstMessage(30 * 1000); // 30 seconds
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue")
               .setHeartbeatValue(new long[]{10000, 10000})
               .setTaskScheduler(taskScheduler());
        registry.setApplicationDestinationPrefixes("/app");
        registry.setPreservePublishOrder(true);
    }
    
    @Bean
    public org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler taskScheduler() {
        org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler scheduler = new org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        scheduler.setThreadNamePrefix("websocket-heartbeat-thread-");
        scheduler.initialize();
        return scheduler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5173")
                .withSockJS()
                .setHeartbeatTime(25000) // Set heartbeat to 25 seconds
                .setDisconnectDelay(5000); // Set disconnect delay to 5 seconds
    }

    /**
     * Enable WebSocket compression using system property
     * This is a workaround since the direct methods on WsServerContainer are not available
     */
    @Bean
    public ServletContextInitializer enableWsCompression() {
        // Enable WebSocket compression using system property
        // This is more reliable than trying to access WsServerContainer methods directly
        System.setProperty("org.apache.tomcat.websocket.DISABLE_BUILTIN_EXTENSIONS", "false");

        return (ServletContext sc) -> {
            // This ServletContextInitializer doesn't need to do anything specific
            // The system property above handles the compression configuration
        };
    }
}