package com.diamond.saloon.dto;

import lombok.Data;



@Data
public class CartItemDto {

	private String productId;
	
	private String productName;
	
	private double price;
	
	private int quantity;
	
	private String imageUrl;
}
