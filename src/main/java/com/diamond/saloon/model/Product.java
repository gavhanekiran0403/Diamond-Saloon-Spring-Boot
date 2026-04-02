package com.diamond.saloon.model;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "products")
@Data
public class Product {

	@Id
	private String productId;

	@Indexed
	private String productName;

	@Indexed
	private String productCategoryId;

	@Indexed
	private String categoryName;

	@Indexed
	private String brand;

	private String description;

	// for adding product color, size/quantity in ml, gm, pcs
	private Map<String, String> attributes;

	@Indexed
	private Double price;

	private Integer stockQuantity;

	private String imageUrl;

	private Boolean available;
	
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;


}
