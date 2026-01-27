package com.diamond.saloon.dto;

import lombok.Data;

@Data
public class AddToCartDto {

	private String userId;
	
	private String productId;
	
	private int quantity;
}
