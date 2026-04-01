package com.diamond.saloon.dto;

import java.util.List;

import com.diamond.saloon.enums.PaymentMethod;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CartCheckoutDto {

	@Size(max = 10, message = "Cannot checkout more than 10 items")
	private List<String> cartItemIds;

	@NotBlank(message = "Address id is required")
	private String addressId;

	@NotNull(message = "Payment method is required")
	private PaymentMethod paymentMethod;
}
