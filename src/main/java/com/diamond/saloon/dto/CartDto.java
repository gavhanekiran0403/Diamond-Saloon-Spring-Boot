package com.diamond.saloon.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CartDto {

	private String cartId;

	@NotBlank(message = "User id is required")
	private String userId;

	@Valid
	@Size(max = 10, message = "Cart cannot have more than 10 items")
	private List<CartItemDto> items;

	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
	private double totalAmount;

}
