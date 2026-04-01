package com.diamond.saloon.model;

import java.util.UUID;

import lombok.Data;

@Data
public class CartItem {

	private String cartItemId;

	private String productId;

	private String productName;

	private double price;

	private int quantity;

	private boolean inStock; // used for ui stock indication

	private String stockMessage;

	private String imageUrl;

	public CartItem() {
		this.cartItemId = UUID.randomUUID().toString();
		this.inStock = true;
	}
}
