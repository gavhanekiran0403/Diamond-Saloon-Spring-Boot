package com.diamond.saloon.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;

import lombok.Data;


@Data
public class OrderItem {

	private String ordeItemId;
	
	private String orderId;
	
	private String productId;
	
	private String productName;
	
	private double price;
	
	private int quantity;
	
	public OrderItem() {
		this.ordeItemId = UUID.randomUUID().toString().substring(0, 12);
	}
	
}
