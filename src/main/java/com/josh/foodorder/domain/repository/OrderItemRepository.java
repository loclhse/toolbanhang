package com.josh.foodorder.domain.repository;

import com.josh.foodorder.domain.model.OrderItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends MongoRepository<OrderItem, UUID> {
    
    // Find all order items by order ID
    List<OrderItem> findByOrderId(UUID orderId);
    
    // Find order item by order ID and item ID
    OrderItem findByOrderIdAndId(UUID orderId, UUID itemId);
    
    // Find marked order items by order ID
    List<OrderItem> findByOrderIdAndIsMarkedTrue(UUID orderId);
} 