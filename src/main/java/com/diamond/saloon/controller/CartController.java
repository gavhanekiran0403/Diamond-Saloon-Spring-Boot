package com.diamond.saloon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.common.ApiResponse;
import com.diamond.saloon.common.ApiResponseUtil;
import com.diamond.saloon.dto.AddToCartDto;
import com.diamond.saloon.dto.CartDto;
import com.diamond.saloon.dto.MergeCartDto;
import com.diamond.saloon.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	
	// add products in cart
	@PostMapping
	public ResponseEntity<ApiResponse<CartDto>> addToCart(
			@Valid @RequestBody AddToCartDto cartDto, HttpServletRequest request) {

		CartDto cart = cartService.addToCart(cartDto);
		
		return ApiResponseUtil.success(
				cart,
				"Product added to cart successfully",
				HttpStatus.CREATED,
				request
		);
	}

	
	// get cart
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<CartDto>> getCart(
			@PathVariable String userId, HttpServletRequest request) {

		CartDto cart = cartService.getCart(userId);
		
		return ApiResponseUtil.success(
				cart,
				"Cart fetched successfully",
				HttpStatus.OK,
				request
		);
	}

	
	// get cart item count
	@GetMapping("/{userId}/count")
	public ResponseEntity<ApiResponse<Integer>> getCartCount(
			@PathVariable String userId, HttpServletRequest request) {

		int count = cartService.getCartItemCount(userId);
		
		return ApiResponseUtil.success(
				count,
				"Cart item count fetched successfully",
				HttpStatus.OK,
				request
		);
	}
	
	// Validate cart
	@GetMapping("/{userId}/validate")
	public ResponseEntity<ApiResponse<CartDto>> validateCart(
			@PathVariable String userId, HttpServletRequest request) {

		CartDto cart = cartService.validateCart(userId);
		
		return ApiResponseUtil.success(
				cart,
				"Cart validated successfully",
				HttpStatus.OK,
				request
		);
		
	}
	
	
	// Merge cart
	@PostMapping("/merge")
	public ResponseEntity<ApiResponse<CartDto>> mergeCart(
			@Valid @RequestBody MergeCartDto dto, HttpServletRequest request) {

		CartDto cart = cartService.mergeCart(dto);
		
		return ApiResponseUtil.success(
				cart,
				"Cart merged successfully",
				HttpStatus.OK,
				request
		);
	}
	
	
	// update cart
	@PutMapping("/{userId}/items/{cartItemId}")
	public ResponseEntity<ApiResponse<CartDto>> updateCartItem(
			@PathVariable String userId, @PathVariable String cartItemId, 
			@RequestParam int quantity, HttpServletRequest request) {

		CartDto cart = cartService.updateCart(userId, cartItemId, quantity);
		
		return ApiResponseUtil.success(
				cart,
				"Cart updated successfully",
				HttpStatus.OK,
				request
		);
	}

	
	// delete product from cart
	@DeleteMapping("/{userId}/items/{cartItemId}")
	public ResponseEntity<ApiResponse<Void>> removeCartItem(@PathVariable String userId,
			@PathVariable String cartItemId, HttpServletRequest request) {

		cartService.removeProduct(userId, cartItemId);
		
		return ApiResponseUtil.successMessage(
				"Product removed from cart successfully",
				HttpStatus.OK,
				request
		);
	}

	
	// clear cart/ empty cart
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse<Void>> clearCart(
			@PathVariable String userId, HttpServletRequest request) {

		cartService.clearCart(userId);
		
		return ApiResponseUtil.successMessage(
				"Cart cleared successfully",
				HttpStatus.OK,
				request
		);
	}

}
