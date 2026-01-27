package com.diamond.saloon.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.diamond.saloon.model.CartItem;

import lombok.Data;

@Data
public class CartDto {

	private String cartId;
	
	private String userId;
	
	private List<CartItem> products;
	
	private double totalAmount;
	
	private LocalDateTime updatedAt;
}
