package com.josh.foodorder.dto.response;

public class MarkItemResponseDTO {
    private String orderId;
    private String itemId;
    private boolean isMarked;


    public MarkItemResponseDTO() {}

    public MarkItemResponseDTO(String orderId, String itemId, boolean isMarked) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.isMarked = isMarked;

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }


}