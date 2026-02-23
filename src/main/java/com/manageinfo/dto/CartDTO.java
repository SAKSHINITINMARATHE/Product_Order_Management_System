package com.manageinfo.dto;

import java.util.List;

public class CartDTO {
    private Long id;
    private List<CartItemDTO> items;
    private double totalAmount;

    public CartDTO() {
    }

    public CartDTO(Long id, List<CartItemDTO> items, double totalAmount) {
        this.id = id;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
