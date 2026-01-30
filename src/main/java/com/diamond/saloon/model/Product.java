package com.diamond.saloon.model;

import java.util.Map;

import org.springframework.data.annotation.Id; 
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "product")
@Data
public class Product {

	@Id
	private String productId;
	
	private String productName;
	
	private String productCategoryId;
	
	private String brand;
	
	private String description;
	
	// for adding product color, size/quantity in ml, gm, pcs 
	private Map<String, String> attributes; 
	
	private double price;
	
	private int stockQuantity;
	
	private String imageUrl;
	
	private boolean isAvailable;
	
	
	
	
}
