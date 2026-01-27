package com.diamond.saloon.service;

import com.diamond.saloon.dto.AddToCartDto;
import com.diamond.saloon.dto.CartDto;

public interface CartService {

	public CartDto addToCart(AddToCartDto requestDto);
}
