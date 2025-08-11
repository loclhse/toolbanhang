package com.josh.foodorder.dto.request;

public class MarkItemRequestDTO {
    private boolean isMarked;

    public MarkItemRequestDTO() {}

    public MarkItemRequestDTO(boolean isMarked) {
        this.isMarked = isMarked;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }
}