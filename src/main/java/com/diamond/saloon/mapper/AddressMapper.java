package com.diamond.saloon.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.AddressDto;
import com.diamond.saloon.model.Address;

@Component
public class AddressMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	public Address toEntity(AddressDto dto) {
		return modelMapper.map(dto, Address.class);
	}
	
	public AddressDto toDto(Address entity) {
		return modelMapper.map(entity, AddressDto.class);
	}
	
}
