package com.josh.foodorder.dto.request;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddOrderItemRequestDTO {
    private UUID foodItemId;
    private int quantity;

    public AddOrderItemRequestDTO() {}

    public AddOrderItemRequestDTO(UUID foodItemId, int quantity) {
        this.foodItemId = foodItemId;
        this.quantity = quantity;
    }

    public UUID getFoodItemId() { return foodItemId; }
    public void setFoodItemId(UUID foodItemId) { this.foodItemId = foodItemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
} 