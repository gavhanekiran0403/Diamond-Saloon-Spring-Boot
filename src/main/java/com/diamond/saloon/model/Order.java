package com.diamond.saloon.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.diamond.saloon.enums.OrderStatus;
import com.diamond.saloon.enums.PaymentMethod;
import com.diamond.saloon.enums.PaymentStatus;

import lombok.Data;

@Document(collection = "orders")
@Data
public class Order {

	@Id
	private String orderId;

	@Indexed(unique = true, sparse = true)
	private String orderNumber;

	@Indexed
	private String userId;

	private List<OrderItem> items;

	private double totalAmount;

	private OrderAddress deliveryAddress;

	@Indexed
	private OrderStatus orderStatus;

	@Indexed
	private PaymentStatus paymentStatus;

	private PaymentMethod paymentMethod;

	private String returnReason;

	@Indexed
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private LocalDateTime deliveredAt;

}
