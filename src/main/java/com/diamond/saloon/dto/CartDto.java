package com.diamond.saloon.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.diamond.saloon.model.CartItem;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartDto {

	private String cartId;
	
	@NotBlank(message = "User id is required")
	private String userId;
	
	@NotEmpty(message = "Cart must contain at least one product")
	@Valid
	private List<CartItemDto> products;
	
	@NotNull(message = "Total amount is required")
	@Min(value = 0, message = "Total amount cannot be negative")
	private double totalAmount;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy - hh:mma")
	private LocalDateTime updatedAt;
}
