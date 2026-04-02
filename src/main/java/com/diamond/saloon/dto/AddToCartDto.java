package com.diamond.saloon.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min; 
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddToCartDto {

	@NotBlank(message = "User id is required")
	private String userId;

	@NotBlank(message = "Product id is required")
	private String productId;

	@Min(value = 1, message = "Quantity must be at least 1")
	@Max(value = 10, message = "Maximum 10 quantity allowed per request")
	private int quantity;
}
