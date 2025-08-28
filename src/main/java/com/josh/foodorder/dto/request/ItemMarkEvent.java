package com.josh.foodorder.dto.request;

public class ItemMarkEvent {
    private String orderId;
    private String itemId;
    private boolean marked;

    public ItemMarkEvent() {}

    public ItemMarkEvent(String orderId, String itemId, boolean marked) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.marked = marked;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public boolean isMarked() { return marked; }
    public void setMarked(boolean marked) { this.marked = marked; }
}
