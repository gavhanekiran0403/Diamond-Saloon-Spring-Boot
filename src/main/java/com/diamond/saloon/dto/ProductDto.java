package com.diamond.saloon.dto;

import java.util.Map;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductDto {
	
	private String productId;
	
	@NotBlank(message = "Product name is required")
	private String productName;
	
	@NotBlank(message = "Product category id is required")
	private String productCategoryId;
	
	@NotBlank(message = "Brand name is required")
	private String brand;
	
	private String description;
	
	private Map<@NotBlank(message = "Attributes key cannot be blank")String, 
				@NotBlank(message = "Attributes value cannot be blank")String> attributes; 
	
	@NotNull(message = "Price is required")
	@Positive(message = "Price must be greater than zero")
	private double price;
	
	@NotNull(message = "Stock quantity is required")
	@Min(value = 0, message = "Stock quantity cannot be negative")
	private int stockQuantity;
	
	private String imageUrl;
	
	private boolean isAvailable;
	
	
}
