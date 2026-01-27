package com.diamond.saloon.dto;

import java.util.Map;

import lombok.Data;

@Data
public class ProductDto {
	
private String productId;
	
	private String productName;
	
	private String productCategoryId;
	
	private String brand;
	
	private String description;
	
	private Map<String, String> attributes; 
	
	private double price;
	
	private int stockQuantity;
	
	private String imageUrl;
	
	private boolean isAvailable;
	
	
}
