package com.josh.foodorder.dto.request;

public class TableCreateRequestDTO {
    private int number;

    public TableCreateRequestDTO() {}

    public TableCreateRequestDTO(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
} 