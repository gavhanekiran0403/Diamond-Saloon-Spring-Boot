package com.diamond.saloon.dto;

import lombok.Data;

@Data
public class ProductDto {

	private String productName;
	
	private String brand;
	
	private double price;
	
	private int stockQuantity;
	
	private String description;
	
	private String imageUrl;
	
	private boolean isAvailable;
	
	private String productCategoryId;
}
