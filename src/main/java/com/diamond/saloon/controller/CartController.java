package com.diamond.saloon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	// get cart
	@GetMapping("/{userId}")
	public CartDto getCart(@PathVariable String userId) {
		return cartService.getCart(userId);
		
	}
	
	// update cart
	@PutMapping("/update/{cartItemId}")
	public CartDto updateCart(@PathVariable String cartItemId,
							  @RequestParam String userId,
							  @RequestParam int quantity) {
		return cartService.updateCart(userId, cartItemId, quantity);
		
	}
	
	// delete product from cart
	@DeleteMapping("/{userId}/item/{cartItemId}")
	public String removeProduct(@PathVariable String userId, @PathVariable String cartItemId) {
		cartService.removeProduct(userId, cartItemId);
		return "Product removed from cart";
	}

	// clear cart/ empty cart
	@DeleteMapping("/{userId}")
	public String clearCart(@PathVariable String userId) {
		cartService.clearCart(userId);
		return "Cart cleared successfully";
	}
	
}
