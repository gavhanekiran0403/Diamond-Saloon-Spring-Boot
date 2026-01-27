package com.diamond.saloon.responsedto;

import lombok.Data;

@Data
public class ProductResponseDto {
	
	private String productId;

	private String productName;
	
	private String categoryId;
	
	private String categoryName;
	
	private String brand;
	
	private double price;
	
	private int stockQuantity;
	
	private String description;
	
	private boolean isAvailable;
	
	private String imageUrl;
	
}
