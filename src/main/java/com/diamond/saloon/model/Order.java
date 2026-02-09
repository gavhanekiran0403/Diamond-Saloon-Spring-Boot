package com.diamond.saloon.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.diamond.saloon.enums.OrderStatus;
import com.diamond.saloon.enums.PaymentStatus;

import lombok.Data;

@Document(collection = "orders")
@Data
public class Order {

	@Id
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
