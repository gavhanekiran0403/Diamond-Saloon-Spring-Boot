package com.diamond.saloon.dto;

import java.time.LocalDateTime;

import com.diamond.saloon.enums.PaymentMethod;
import com.diamond.saloon.enums.PaymentStatus;

import lombok.Data;

@Data
public class PaymentDto {

	private String paymentId;
	
	private String userId;
	
	private String orderId;
	
	private double amount; 
	
	private PaymentMethod paymentMethod;
	
	private PaymentStatus paymentStatus;
	
	private String transactionId;
	
	private LocalDateTime paymentDate;
}
