package com.diamond.saloon.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.PaymentDto;
import com.diamond.saloon.model.Payment;

@Component
public class PaymentMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	public Payment toEntity(PaymentDto dto) {
		return modelMapper.map(dto, Payment.class);
	}
	
	public PaymentDto toDto(Payment entity) {
		return modelMapper.map(entity, PaymentDto.class);
	}
}
