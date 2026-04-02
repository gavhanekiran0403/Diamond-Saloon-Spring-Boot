package com.diamond.saloon.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderItemDto {

	private String orderItemId;

	@NotBlank(message = "Product id is required")
	private String productId;

	@NotBlank(message = "Product name is required")
	@Size(max = 100, message = "Product name cannot exceed 100 characters")
	private String productName;

	@NotBlank(message = "Brand is required")
	@Size(max = 50, message = "Brand cannot exceed 50 characters")
	private String brand;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
	private double price;

	@NotNull(message = "Quantity is required")
	@Min(value = 1, message = "Quantity must be at least 1")
	private int quantity;

	private String imageUrl;
}
