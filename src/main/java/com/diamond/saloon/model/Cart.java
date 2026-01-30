package com.diamond.saloon.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;



@Document(collection = "carts")
@Data
public class Cart {

	@Id
	private String cartId;
	
	private String userId;
	
	private List<CartItem> products = new ArrayList<>();
	
	private double totalAmount;
	
	private LocalDateTime updatedAt;
	
}
