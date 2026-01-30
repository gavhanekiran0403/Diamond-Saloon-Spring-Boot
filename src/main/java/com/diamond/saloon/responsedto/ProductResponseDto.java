package com.diamond.saloon.responsedto;

import java.util.Map;

import lombok.Data;

@Data
public class ProductResponseDto {
	
	private String productId;

	private String productName;
	
	private String productCategoryId;
	
	private String categoryName;
	
	private String brand;
	
	private String description;
	
	private Map<String, String> attributes; 
	
	private double price;
	
	private int stockQuantity;
	
	private boolean isAvailable;
	
	private String imageUrl;
	
}
