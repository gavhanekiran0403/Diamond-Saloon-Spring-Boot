package com.diamond.saloon.service;

import com.diamond.saloon.dto.AddToCartDto;
import com.diamond.saloon.dto.CartDto;

public interface CartService {

	public CartDto addToCart(AddToCartDto requestDto);
	
	public CartDto getCart(String userId);
	
	public CartDto updateCart(AddToCartDto requestDto);
	
	public CartDto removeProduct(String userId, String productId);
	
	public CartDto clearCart(String userId);
}
