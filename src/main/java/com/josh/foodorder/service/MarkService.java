package com.josh.foodorder.service;

import com.josh.foodorder.domain.model.Order;
import com.josh.foodorder.domain.model.OrderItem;
import com.josh.foodorder.domain.repository.OrderRepository;
import com.josh.foodorder.domain.repository.OrderItemRepository;
import com.josh.foodorder.dto.response.MarkItemResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MarkService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;

        public void markItem(String orderId, String itemId, boolean isMarked) {
        try {
            UUID orderUUID = UUID.fromString(orderId);
            UUID itemUUID = UUID.fromString(itemId);
            
            // Verify order exists
            Order order = orderRepository.findById(orderUUID).orElseThrow(
                () -> new RuntimeException("Order not found: " + orderId)
            );
            
            // Find the specific order item in the separate collection
            OrderItem orderItem = orderItemRepository.findByOrderIdAndId(orderUUID, itemUUID);
            if (orderItem == null) {
                throw new RuntimeException("Order item not found: " + itemId);
            }
            
            // Update the marked status
            orderItem.setMarked(isMarked);
            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            
            System.out.println("✅ Item marked: " + itemId + " in order: " + orderId + " -> " + isMarked);
            System.out.println("✅ Verified saved item marked status: " + savedOrderItem.isMarked());
            
            // Broadcast the change to all connected clients via WebSocket
            MarkItemResponseDTO message = new MarkItemResponseDTO(orderId, itemId, isMarked);
            messagingTemplate.convertAndSend("/topic/marks", message);
            System.out.println("📡 WebSocket broadcast sent for mark update: " + message);
            
        } catch (Exception e) {
            System.err.println("Error marking item: " + e.getMessage());
            throw new RuntimeException("Failed to mark item: " + e.getMessage());
        }
    }

    public List<String> getMarkedItems(String orderId) {
        try {
            UUID orderUUID = UUID.fromString(orderId);
            List<OrderItem> markedItems = orderItemRepository.findByOrderIdAndIsMarkedTrue(orderUUID);
            return markedItems.stream()
                .map(item -> item.getId().toString())
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error getting marked items: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    public boolean isItemMarked(String orderId, String itemId) {
        try {
            UUID orderUUID = UUID.fromString(orderId);
            UUID itemUUID = UUID.fromString(itemId);
            
            OrderItem orderItem = orderItemRepository.findByOrderIdAndId(orderUUID, itemUUID);
            return orderItem != null && orderItem.isMarked();
        } catch (Exception e) {
            System.err.println("Error checking if item is marked: " + e.getMessage());
            return false;
        }
    }

    public void clearMarkedItems(String orderId) {
        try {
            UUID orderUUID = UUID.fromString(orderId);
            List<OrderItem> markedItems = orderItemRepository.findByOrderIdAndIsMarkedTrue(orderUUID);
            
            for (OrderItem item : markedItems) {
                item.setMarked(false);
                orderItemRepository.save(item);
            }
            
            if (!markedItems.isEmpty()) {
                System.out.println("🧹 Cleared all marked items for order: " + orderId);
            }
        } catch (Exception e) {
            System.err.println("Error clearing marked items: " + e.getMessage());
        }
    }
    
    // Get all order items for an order (for UI display)
    public List<OrderItem> getOrderItems(String orderId) {
        try {
            UUID orderUUID = UUID.fromString(orderId);
            return orderItemRepository.findByOrderId(orderUUID);
        } catch (Exception e) {
            System.err.println("Error getting order items: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}