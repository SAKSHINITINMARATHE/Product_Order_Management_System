package com.manageinfo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products_info")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "Product_Name", nullable = false)
	private String name;

	@Column(name = "Product_Tagline", nullable = false)
	private String tagline;

	@Column(name = "Product_Description")
	private String description;

	@Column(name = "Product_Price", nullable = false)
	private int price;

	@Column(name = "Product_Quantity", nullable = false)
	private int quantity;

	@Column(name = "Is_Active", nullable = false)
	private boolean isActive = true;

	@Column(name = "Is_Deleted", nullable = false)
	private boolean isDeleted = false;

	public Product() {
	}

	public Product(int id, String name, String tagline, String description, int price, int quantity, boolean isActive) {
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

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", isActive=" + isActive + ", isDeleted=" + isDeleted + "]";
	}
}
