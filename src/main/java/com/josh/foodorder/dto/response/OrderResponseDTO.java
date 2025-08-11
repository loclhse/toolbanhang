package com.josh.foodorder.dto.response;

import com.josh.foodorder.domain.model.Order;
import com.josh.foodorder.domain.model.OrderItem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderResponseDTO {
    private UUID id;
    private List<Integer> tableNumbers;
    private int numberOfPeople;
    private List<OrderItemResponseDTO> items;
    private String status;
    private LocalDateTime createdAt;
    private double totalAmount;
    private boolean isDeleted;
   

    // Constructor from Order entity
    public OrderResponseDTO(Order order, List<Integer> tableNumbers) {
        this.id = order.getId();
        this.tableNumbers = tableNumbers;
        this.numberOfPeople = order.getNumberOfPeople();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
        this.isDeleted = order.isDeleted();
        
        // Convert OrderItems to DTOs
        if (order.getItems() != null) {
            this.items = order.getItems().stream()
                .map(OrderItemResponseDTO::new)
                .collect(Collectors.toList());
            
            // Calculate total amount
            this.totalAmount = this.items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        }
    }

    // Getters
    public UUID getId() { return id; }
    public List<Integer> getTableNumbers() { return tableNumbers; }
    public int getNumberOfPeople() { return numberOfPeople; }
    public List<OrderItemResponseDTO> getItems() { return items; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public double getTotalAmount() { return totalAmount; }
    public boolean isDeleted() { return isDeleted; }

    public static class OrderItemResponseDTO {
        private UUID id;
        private String foodItemName;
        private double price;
        private int quantity;
        private double subtotal;
        private boolean isMarked;

        public OrderItemResponseDTO(OrderItem orderItem) {
            this.id = orderItem.getId();
            this.foodItemName = orderItem.getFoodItem().getName();
            this.price = orderItem.getFoodItem().getPrice();
            this.quantity = orderItem.getQuantity();
            this.subtotal = this.price * this.quantity;
            this.isMarked = orderItem.isMarked();
        }

        // Getters
        public UUID getId() { return id; }
        public String getFoodItemName() { return foodItemName; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public double getSubtotal() { return subtotal; }
        public boolean isMarked() { return isMarked; }
    }
} 