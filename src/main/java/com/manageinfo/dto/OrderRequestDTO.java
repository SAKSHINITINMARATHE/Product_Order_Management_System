package com.manageinfo.dto;

public class OrderRequestDTO {
    private Long userId;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
