package com.diamond.saloon.mapper;

import com.diamond.saloon.dto.RegisterDto; 
import com.diamond.saloon.enums.Role;
import com.diamond.saloon.model.User;
import com.diamond.saloon.responsedto.UserResponseDto;

public class UserMapper {
	
	
	public static UserResponseDto toDto(User user) {
		
		UserResponseDto dto = new UserResponseDto();
		
		dto.setUserId(user.getUserId());
		dto.setFullName(user.getFullName());
		dto.setEmail(user.getEmail());
		dto.setPhone(user.getPhone());
		dto.setRole(user.getRole());
		
		return dto;
	}
}
