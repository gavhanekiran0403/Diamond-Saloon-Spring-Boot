package com.diamond.saloon.dto;

import lombok.Data;

@Data
public class OrderItemDto {

	private String ordeItemId;
	
	private String productId;
	
	private String productName;
	
	private double price;
	
	private int quantity;
}
