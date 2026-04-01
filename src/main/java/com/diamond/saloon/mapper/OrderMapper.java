package com.diamond.saloon.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.OrderDto;
import com.diamond.saloon.model.Order;

@Component
public class OrderMapper {

	@Autowired
	private ModelMapper modelMapper;

	public Order toEntity(OrderDto dto) {
		return modelMapper.map(dto, Order.class);
	}

	public OrderDto toDto(Order order) {
		return modelMapper.map(order, OrderDto.class);
	}

}
