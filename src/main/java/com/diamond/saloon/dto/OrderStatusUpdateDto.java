package com.diamond.saloon.dto;

import com.diamond.saloon.enums.OrderStatus;

import lombok.Data;

@Data
public class OrderStatusUpdateDto {

	private String orderId;
	
	private OrderStatus status;
}
