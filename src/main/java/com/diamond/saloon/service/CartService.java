package com.diamond.saloon.service;

import com.diamond.saloon.dto.AddToCartDto;
import com.diamond.saloon.dto.CartDto;

public interface CartService {

	public CartDto addToCart(AddToCartDto requestDto);
	
	public CartDto getCart(String userId);
	
	public CartDto updateCart(String userId, String cartItemId, int quantity);
	
	public void removeProduct(String userId, String cartItemId);
	
	public void clearCart(String userId);
}
