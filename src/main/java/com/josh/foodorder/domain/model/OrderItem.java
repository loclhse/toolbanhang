package com.josh.foodorder.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;

@Document(collection = "order_items")
public class OrderItem {
    @Id
    private UUID id;

    @DBRef
    private Order order;

    @DBRef
    private FoodItem foodItem;

    private int quantity;
    private boolean isMarked = false;

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public FoodItem getFoodItem() { return foodItem; }
    public void setFoodItem(FoodItem foodItem) { this.foodItem = foodItem; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public boolean isMarked() { return isMarked; }
    public void setMarked(boolean marked) { isMarked = marked; }
} 