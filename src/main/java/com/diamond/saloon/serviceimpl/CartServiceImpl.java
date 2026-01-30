package com.diamond.saloon.serviceimpl;

import java.time.LocalDateTime; 
import java.util.ArrayList;
import java.util.UUID;

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
		
		if(product.getStockQuantity()<=0) {
			throw new BadRequestException("Product  is out of stock");
		}
		
		if(requestDto.getQuantity() > product.getStockQuantity()) {
			throw new BadRequestException("Insufficient stock available");
		}
		
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
				
		for(CartItem item : cart.getProducts()) {
			Product product = productRepository.findById(item.getProductId())
					.orElse(null);
			
			if(product == null || product.getStockQuantity()<=0) {
				item.setQuantity(0);
				item.setPrice(0);
			}
			else {
				item.setPrice(product.getPrice());
				
				if(item.getQuantity() > product.getStockQuantity()) {
					item.setQuantity(product.getStockQuantity());
				}
			}
		}
		
		cart.getProducts().removeIf(i -> i.getQuantity() <= 0);
		
		calculateTotal(cart);
		
		return cartMapper.toDto(cart);
	}

	// update cart
	@Override
	public CartDto updateCart(String userId, String cartItemId, int quantity) {

		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

		CartItem cartItem = cart.getProducts().stream()
				.filter(item -> item.getCartItemId().equals(cartItemId))
				.findFirst()
				.orElseThrow(() -> new BadRequestException("Product not found in cart"));

		Product product = productRepository.findById(cartItem.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		if(quantity <=0) {
			cart.getProducts().remove(cartItem);	
		}
		else {
			if (product.getStockQuantity() < quantity) {
			throw new BadRequestException("Insufficient stock available");
			}
			cartItem.setQuantity(quantity);
			cartItem.setPrice(product.getPrice());
		}
		

		calculateTotal(cart);
		cart.setUpdatedAt(LocalDateTime.now());

		return cartMapper.toDto(cartRepository.save(cart));
	}

	// remove product from cart
	@Override
	public void removeProduct(String userId, String cartItemId) {
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("cart not found"));
		
		boolean removed = cart.getProducts().removeIf(item -> item.getCartItemId().equals(cartItemId));
		
		if(!removed) {
			throw new ResourceNotFoundException("cart item not found");
		}
		calculateTotal(cart);
		cart.setUpdatedAt(LocalDateTime.now());
		
		cartRepository.save(cart);
	}

	
	// clear cart
	@Override
	public void clearCart(String userId) {
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("cart not found"));
		
		cart.getProducts().clear();
		calculateTotal(cart);
		cart.setUpdatedAt(LocalDateTime.now());
		
		cartRepository.save(cart);
	}

}
