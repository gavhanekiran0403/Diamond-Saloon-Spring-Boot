package com.diamond.saloon.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequestDto {

	private String userId;
	
	private List<String> cartItemIds;
	
	private String productId;
	private int quantity;
	
	private String addressId;
	
	private String paymentMethod;
	
}
