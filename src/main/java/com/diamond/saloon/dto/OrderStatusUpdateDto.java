package com.diamond.saloon.dto;

import com.diamond.saloon.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusUpdateDto {

	@NotNull(message = "Order status is required")
	private OrderStatus status;
}
