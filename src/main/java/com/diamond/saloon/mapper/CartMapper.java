package com.diamond.saloon.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.CartDto;
import com.diamond.saloon.model.Cart;

@Component
public class CartMapper {

	@Autowired
	private ModelMapper modelMapper;

	public Cart toEntity(CartDto dto) {
		return modelMapper.map(dto, Cart.class);
	}

	public CartDto toDto(Cart cart) {
		return modelMapper.map(cart, CartDto.class);
	}
}
