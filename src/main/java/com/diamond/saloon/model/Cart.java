package com.diamond.saloon.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "carts")
@Data
public class Cart {

	@Id
	private String cartId;

	@Indexed(unique = true)
	private String userId;

	private List<CartItem> items = new ArrayList<>();

	private double totalAmount;
	
	@Indexed
	private LocalDateTime createdAt;

	@Indexed
	private LocalDateTime updatedAt;

}
