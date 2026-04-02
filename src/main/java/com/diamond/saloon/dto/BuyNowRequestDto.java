package com.diamond.saloon.dto;

import com.diamond.saloon.enums.PaymentMethod;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BuyNowRequestDto {

	@NotBlank(message = "Product id is required")
	private String productId;

	@Min(value = 1, message = "Quantity must be at least 1")
	private int quantity;

	@NotBlank(message = "Address id is required")
	private String addressId;

	@NotNull(message = "Payment method is required")
	private PaymentMethod paymentMethod;
}
