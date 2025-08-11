package com.josh.foodorder.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "payments")
public class Payment {
    @Id
    private UUID id;
    private UUID orderId;
    private List<UUID> tableIds;
    private List<PaymentItem> items;
    private double totalAmount;
    private String img;
    private LocalDateTime orderCreatedAt;
    private LocalDateTime paidAt;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    
    public enum PaymentStatus {
        PENDING, PAID
    }
    public enum PaymentMethod {
        CASH, BANK_TRANSFER
    }

    public Payment() {
        this.id = UUID.randomUUID();
        this.paidAt = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    
    public List<UUID> getTableIds() { return tableIds; }
    public void setTableIds(List<UUID> tableIds) { this.tableIds = tableIds; }
    
    public List<PaymentItem> getItems() { return items; }
    public void setItems(List<PaymentItem> items) { this.items = items; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public LocalDateTime getOrderCreatedAt() { return orderCreatedAt; }
    public void setOrderCreatedAt(LocalDateTime orderCreatedAt) { this.orderCreatedAt = orderCreatedAt; }
    
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
    
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    
   
    
   
    public static class PaymentItem {
        private String foodItemName;
        private double price;
        private int quantity;
        private double subtotal;
        
        public PaymentItem() {}
        
        public PaymentItem(String foodItemName, double price, int quantity, double subtotal) {
            this.foodItemName = foodItemName;
            this.price = price;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }
        
       
        public String getFoodItemName() { return foodItemName; }
        public void setFoodItemName(String foodItemName) { this.foodItemName = foodItemName; }
        
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        
        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    }
} 