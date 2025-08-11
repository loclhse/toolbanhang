package com.josh.foodorder.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;

@Document(collection = "food_items")
public class FoodItem {
    @Id
    private UUID id;
    private String name;
    private double price;
    private String img;

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }
} 