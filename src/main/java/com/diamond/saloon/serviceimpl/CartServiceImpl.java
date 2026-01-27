package com.diamond.saloon.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.AddToCartDto;
import com.diamond.saloon.dto.CartDto;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.CartItemMapper;
import com.diamond.saloon.mapper.CartMapper;
import com.diamond.saloon.model.Cart;
import com.diamond.saloon.model.CartItem;
import com.diamond.saloon.model.Product;
import com.diamond.saloon.repository.CartRepository;
import com.diamond.saloon.repository.ProductRepository;
import com.diamond.saloon.service.CartService;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private CartItemMapper cartItemMapper;
	
	
	// Utility Method
	
	private void calculateTotal(Cart cart) {
		double total = cart.getProducts().stream()
				.mapToDouble(item -> item.getPrice() * item.getQuantity())
				.sum();
		cart.setTotalAmount(total);
	}
	
	// add products in cart
	@Override
	public CartDto addToCart(AddToCartDto requestDto) {
		
		Product product = productRepository.findById(requestDto.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		
		Cart cart = cartRepository.findByUserId(requestDto.getUserId())
				.orElseGet(() -> {
					Cart newCart = new Cart();
					newCart.setUserId(requestDto.getUserId());
					newCart.setProducts(new ArrayList<>());
					newCart.setUpdatedAt(LocalDateTime.now());
					return newCart;
				});
		
		CartItem existingItem = cart.getProducts().stream()
				.filter(item -> item.getProductId().equals(product.getProductId()))
				.findFirst()
				.orElse(null);
		
		if(existingItem != null) {
			existingItem.setQuantity(existingItem.getQuantity() + requestDto.getQuantity());
		}else {
			CartItem item = new CartItem();
			item.setProductId(product.getProductId());
			item.setProductName(product.getProductName());
			item.setPrice(product.getPrice());
			item.setQuantity(requestDto.getQuantity());
			item.setImageUrl(product.getImageUrl());
			
			cart.getProducts().add(item);
		}
		
		calculateTotal(cart);
		cart.setUpdatedAt(LocalDateTime.now());
		
		return cartMapper.toDto(cartRepository.save(cart));
	}

}
