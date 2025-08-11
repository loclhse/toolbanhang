package com.josh.foodorder.dto.request;

import java.util.List;
import java.util.UUID;

public class OrderRequestDTO {

    private List<UUID> tableIds;
    private int numberOfPeople;
    private List<OrderItemRequestDTO> items;

    // Default constructor for JSON deserialization
    public OrderRequestDTO() {}

    // Constructor for order creation
    public OrderRequestDTO(List<UUID> tableIds, int numberOfPeople, List<OrderItemRequestDTO> items) {
        this.tableIds = tableIds;
        this.numberOfPeople = numberOfPeople;
        this.items = items;
    }

    // Getters and setters
    public List<UUID> getTableIds() { return tableIds; }
    public void setTableIds(List<UUID> tableIds) { this.tableIds = tableIds; }

    public int getNumberOfPeople() { return numberOfPeople; }
    public void setNumberOfPeople(int numberOfPeople) { this.numberOfPeople = numberOfPeople; }

    public List<OrderItemRequestDTO> getItems() { return items; }
    public void setItems(List<OrderItemRequestDTO> items) { this.items = items; }


    public static class OrderItemRequestDTO {
        private UUID foodItemId;  // Only need the food item ID from the menu
        private int quantity;

        // Default constructor for JSON deserialization
        public OrderItemRequestDTO() {}
        
        // Constructor for order item creation
        public OrderItemRequestDTO(UUID foodItemId, int quantity) {
            this.foodItemId = foodItemId;
            this.quantity = quantity;
        }

        // Getters and setters
        public UUID getFoodItemId() { return foodItemId; }
        public void setFoodItemId(UUID foodItemId) { this.foodItemId = foodItemId; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
} 