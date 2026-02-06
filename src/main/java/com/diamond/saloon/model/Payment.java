package com.diamond.saloon.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.diamond.saloon.enums.PaymentMethod;
import com.diamond.saloon.enums.PaymentStatus;

import lombok.Data;

@Document(collection = "payments")
@Data
public class Payment {

	@Id
	private String paymentId;
	
	private String userId;
	
	private String orderId;
	
	private double amount; 
	
	private PaymentMethod paymentMethod;
	
	private PaymentStatus paymentStatus;
	
	private String transactionId;
	
	private LocalDateTime paymentDate;
	
}
