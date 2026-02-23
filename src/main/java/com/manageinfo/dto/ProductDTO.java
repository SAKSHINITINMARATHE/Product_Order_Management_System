package com.manageinfo.dto;

public class ProductDTO {
    private int id;
    private String name;
    private String tagline;
    private String description;
    private int price;
    private int quantity;
    private boolean isActive;

    public ProductDTO() {
    }

    public ProductDTO(int id, String name, String tagline, String description, int price, int quantity,
            boolean isActive) {
        this.id = id;
        this.name = name;
        this.tagline = tagline;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
