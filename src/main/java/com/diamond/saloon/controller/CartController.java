package com.diamond.saloon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.dto.AddToCartDto;
import com.diamond.saloon.dto.CartDto;
import com.diamond.saloon.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	
	// add products in cart
	@PostMapping("/add")
	public CartDto addToCart(@RequestBody AddToCartDto cartDto) {
		return cartService.addToCart(cartDto);
	}
}
