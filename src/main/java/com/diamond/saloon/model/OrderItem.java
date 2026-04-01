package com.diamond.saloon.model;

import java.util.UUID;

import lombok.Data;

@Data
public class OrderItem {

	private String orderItemId;

	private String productId;

	private String productName;

	private String brand;

	private double price;

	private int quantity;

	private String imageUrl;

	public OrderItem() {
		this.orderItemId = UUID.randomUUID().toString();
	}

}
