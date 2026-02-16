package com.diamond.saloon.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCartDto {

	@NotBlank(message = "User id is required")
	private String userId;
	
	@NotBlank(message = "Product id is required")
	private String productId;
	
	@NotNull(message = "Quantity is required")
	@Min(value = 1, message = "Quantity must be at least 1")
	private int quantity;
}
