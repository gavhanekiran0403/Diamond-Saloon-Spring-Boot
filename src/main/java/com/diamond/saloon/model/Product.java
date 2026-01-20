package com.diamond.saloon.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "product")
@Data
public class Product {

	@Id
	private String productId;
	
	private String productName;
	
	private String brand;
	
	private double price;
	
	private int stockQuantity;
	
	private String description;
	
	private String imageUrl;
	
	private boolean isAvailable;
	
	private String productCategoryId;
	
	
}
