package com.josh.foodorder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
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
        registration.setMessageSizeLimit(128 * 1024) // 128KB (đủ cho order/payment data)
                .setSendBufferSizeLimit(512 * 1024) // 512KB buffer
                .setSendTimeLimit(15 * 1000) // 15 seconds
                .setTimeToFirstMessage(30 * 1000);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Chỉ cần /topic cho broadcast
        registry.enableSimpleBroker("/topic")
                .setHeartbeatValue(new long[]{10000, 10000})
                .setTaskScheduler(taskScheduler());
        // Bỏ setApplicationDestinationPrefixes vì không cần client gửi message
        registry.setPreservePublishOrder(true);
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(4); // Giảm từ 2 xuống 1
        scheduler.setThreadNamePrefix("websocket-heartbeat-");
        scheduler.initialize();
        return scheduler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
                registry.addEndpoint("/ws")
                        .setAllowedOrigins("http://localhost:5173","http://103.90.227.18","http://localhost:3000")
                        .withSockJS()
                        .setHeartbeatTime(25000)
                        .setDisconnectDelay(5000);
    }
}
