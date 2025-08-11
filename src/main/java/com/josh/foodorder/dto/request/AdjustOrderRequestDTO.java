package com.josh.foodorder.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdjustOrderRequestDTO {
    private List<UUID> tableIds;
    private int numberOfPeople;
    private List<OrderItemAdjustmentDTO> items;

    public AdjustOrderRequestDTO() {}

    public AdjustOrderRequestDTO(List<UUID> tableIds, int numberOfPeople, List<OrderItemAdjustmentDTO> items) {
        this.tableIds = tableIds;
        this.numberOfPeople = numberOfPeople;
        this.items = items;
    }

    // Getters and setters
    public List<UUID> getTableIds() { return tableIds; }
    public void setTableIds(List<UUID> tableIds) { this.tableIds = tableIds; }

    public int getNumberOfPeople() { return numberOfPeople; }
    public void setNumberOfPeople(int numberOfPeople) { this.numberOfPeople = numberOfPeople; }

    public List<OrderItemAdjustmentDTO> getItems() { return items; }
    public void setItems(List<OrderItemAdjustmentDTO> items) { this.items = items; }

    public static class OrderItemAdjustmentDTO {
        private UUID foodItemId;
        private int quantity;

        public OrderItemAdjustmentDTO() {}

        public OrderItemAdjustmentDTO(UUID foodItemId, int quantity) {
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