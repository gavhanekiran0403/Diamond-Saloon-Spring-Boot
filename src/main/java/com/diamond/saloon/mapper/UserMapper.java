package com.diamond.saloon.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.diamond.saloon.model.User;
import com.diamond.saloon.responsedto.UserResponseDto;

@Component
public class UserMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public UserResponseDto toDto(User user) {
		
		return modelMapper.map(user, UserResponseDto.class);
	}
}
