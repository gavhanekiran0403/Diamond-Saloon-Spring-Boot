package com.diamond.saloon.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.CartItemDto;
import com.diamond.saloon.model.CartItem;

@Component
public class CartItemMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	public CartItem toEntity(CartItemDto dto) {
		return modelMapper.map(dto, CartItem.class);
	}
	
	public CartItemDto toDto(CartItem cartItem) {
		return modelMapper.map(cartItem, CartItemDto.class);
	}
}
