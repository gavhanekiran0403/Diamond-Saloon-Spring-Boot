package com.diamond.saloon.service;

import com.diamond.saloon.dto.AddToCartDto;
import com.diamond.saloon.dto.CartDto;
import com.diamond.saloon.dto.MergeCartDto;

public interface CartService {

	public CartDto addToCart(AddToCartDto requestDto);

	public CartDto getCart(String userId);

	public int getCartItemCount(String userId);

	public CartDto validateCart(String userId);

	public CartDto mergeCart(MergeCartDto dto);

	public CartDto updateCart(String userId, String cartItemId, int quantity);

	public void removeProduct(String userId, String cartItemId);

	public void clearCart(String userId);

}
