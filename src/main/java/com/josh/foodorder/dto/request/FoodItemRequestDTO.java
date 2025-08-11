package com.josh.foodorder.dto.request;

public class FoodItemRequestDTO {
    private String name;
    private double price;
    private String img;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }
} 