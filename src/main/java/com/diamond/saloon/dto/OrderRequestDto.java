package com.diamond.saloon.dto;

import java.util.List; 

import lombok.Data;

@Data
public class OrderRequestDto {

	private String userId;
	
	private String productId;
	
	private int quantity;
	
	private String cartItemId;
	
	private boolean cartCheckout;
	
	private String addressId;
	
	private String paymentMethod;
	
}
