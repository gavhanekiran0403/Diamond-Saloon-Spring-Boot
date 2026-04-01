package com.diamond.saloon.dto;

import java.util.Map;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductDto {

	private String productId;

	@NotBlank(message = "Product name is required")
	@Size(min = 2, max = 120, message = "Product name must be between 2 and 120 characters")
	private String productName;

	@NotBlank(message = "Product category id is required")
	private String productCategoryId;

	private String categoryName;

	@NotBlank(message = "Brand name is required")
	@Size(min = 2, max = 60, message = "Brand name must be between 2 and 60 characters")
	private String brand;

	@Size(max = 500, message = "Description cannot exceed 500 characters")
	private String description;

	private Map<
		@NotBlank(message = "Attribute key cannot be blank")
		@Size(max = 30, message = "Attribute key too long") String,

		@NotBlank(message = "Attribute value cannot be blank")
		@Size(max = 50, message = "Attribute value too long") String
		> attributes;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.01", message = "Price must be greater than zero")
	private Double price;

	@NotNull(message = "Stock quantity is required")
	@Min(value = 0, message = "Stock quantity cannot be negative")
	private Integer stockQuantity;

	//@NotBlank(message = "Image URL is required")
	private String imageUrl;
	
	private Boolean available;

}
