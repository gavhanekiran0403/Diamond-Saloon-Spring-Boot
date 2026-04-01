package com.diamond.saloon.dto;

import java.util.List;

import com.diamond.saloon.enums.OrderStatus;
import com.diamond.saloon.enums.PaymentMethod;
import com.diamond.saloon.enums.PaymentStatus;
import com.diamond.saloon.model.OrderAddress;
import com.diamond.saloon.model.OrderItem;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderDto {

	private String orderId;

	private String orderNumber;

	@NotBlank(message = "User id is required")
	private String userId;

	@NotEmpty(message = "Order must contain at least one item")
	@Size(max = 50, message = "Order cannot have more than 50 items")
	@Valid
	private List<OrderItem> items;

	@NotNull(message = "Total amount is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
	private double totalAmount;

	@NotNull(message = "Delivery address is required")
	@Valid
	private OrderAddress deliveryAddress;

	@NotNull(message = "Order status is required")
	private OrderStatus orderStatus;

	@NotNull(message = "Payment status is required")
	private PaymentStatus paymentStatus;

	@NotNull(message = "Payment method is required")
	private PaymentMethod paymentMethod;

	@Size(max = 200, message = "Return reason cannot exceed 200 characters")
	private String returnReason;
	
/*
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy - hh:mma")
	private LocalDateTime createdAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy - hh:mma")
	private LocalDateTime updatedAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy - hh:mma")
	private LocalDateTime deliveredAt;
	*/

}
