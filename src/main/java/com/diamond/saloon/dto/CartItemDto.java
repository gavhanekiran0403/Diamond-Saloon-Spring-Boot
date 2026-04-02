package com.diamond.saloon.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CartItemDto {

	private String cartItemId;

	@NotBlank(message = "Product id is required")
	private String productId;

	@NotBlank(message = "Product name is required")
	private String productName;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
	private double price;

	@NotNull(message = "Quantity is required")
	@Min(value = 1, message = "Quantity must be at least 1")
	private int quantity;

	private boolean inStock;

	@Size(max=100, message="Stock message too long")
	private String stockMessage;

	@NotBlank(message = "Image URL is required")
	private String imageUrl;
}
