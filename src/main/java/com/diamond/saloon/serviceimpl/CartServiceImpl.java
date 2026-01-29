package com.diamond.saloon.serviceimpl;

import java.time.LocalDateTime; 
import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.AddToCartDto;
import com.diamond.saloon.dto.CartDto;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.exception.ResourceNotFoundException;
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
			int newQuantity = existingItem.getQuantity() + requestDto.getQuantity();
			
			if(product.getStockQuantity() < newQuantity) {
				throw new BadRequestException("Insufficient stock available");
			}
			existingItem.setQuantity(newQuantity);
			existingItem.setPrice(product.getPrice());
		}else {
			
			if(product.getStockQuantity()<= requestDto.getQuantity()) {
				throw new BadRequestException("Insufficient stock available");
			}
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

	// get user cart
	@Override
	public CartDto getCart(String userId) {
		
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
				
		cart.getProducts().forEach(item -> {
			Product product = productRepository.findById(item.getProductId())
					.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
			
			item.setPrice(product.getPrice());
			
			if(product.getStockQuantity()<=0) {
				throw new  BadRequestException("Product "+product.getProductName()+" is out of stock");
			}
			
			if(item.getQuantity() > product.getStockQuantity()) {
				throw new BadRequestException("Only "+product.getStockQuantity()+" quantity available for product "+product.getProductName());
			}		
		});
		
		calculateTotal(cart);
		
		return cartMapper.toDto(cart);
	}

	// update cart
	@Override
	public CartDto updateCart(AddToCartDto requestDto) {
		
		Cart cart = cartRepository.findByUserId(requestDto.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		
		Product product = productRepository.findById(requestDto.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		
		CartItem cartItem = cart.getProducts().stream()
				.filter(item -> item.getProductId().equals(product.getProductId()))
				.findFirst()
				.orElseThrow(() -> 
						new BadRequestException("Product not found in cart"));
		
		
		if(requestDto.getQuantity() == 0) {
			cart.getProducts().remove(cartItem);
		}
		else {
			if(product.getStockQuantity() < requestDto.getQuantity()) {
				throw new BadRequestException("Insufficient stock available");
			}
				cartItem.setQuantity(requestDto.getQuantity());
				cartItem.setPrice(product.getPrice());
			
		}	
		
		
		calculateTotal(cart);
		cart.setUpdatedAt(LocalDateTime.now());
		
		return cartMapper.toDto(cartRepository.save(cart));
	}

	// remove product from cart
	@Override
	public CartDto removeProduct(String userId, String productId) {
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("cart not found"));
		
		cart.getProducts().removeIf(item -> item.getProductId().equals(productId));
		calculateTotal(cart);
		cart.setUpdatedAt(LocalDateTime.now());
		
		return cartMapper.toDto(cartRepository.save(cart));
	}

	
	// clear cart
	@Override
	public CartDto clearCart(String userId) {
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("cart not found"));
		
		cart.getProducts().clear();
		calculateTotal(cart);
		cart.setUpdatedAt(LocalDateTime.now());
		
		return cartMapper.toDto(cartRepository.save(cart));
	}

}
