package com.diamond.saloon.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.diamond.saloon.enums.OrderStatus;
import com.diamond.saloon.enums.PaymentStatus;
import com.diamond.saloon.model.OrderAddress;
import com.diamond.saloon.model.OrderItem;

import lombok.Data;

@Data
public class OrderDto {

	private String orderId;
	
	private String userId;
	
	private List<OrderItem> items;
	
	private double totalAmount;
	
	private OrderAddress deliveryAddress;
	
	private OrderStatus orderStatus;
	
	private PaymentStatus paymentStatus;
	
	private LocalDateTime orderAt;
	
	private LocalDateTime deliveredAt;
}
