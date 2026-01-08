package com.diamond.saloon.mapper;

import com.diamond.saloon.dto.UserDto;
import com.diamond.saloon.enums.Role;
import com.diamond.saloon.model.User;
import com.diamond.saloon.responsedto.UserResponseDto;

public class UserMapper {
	
	public static UserResponseDto toDto(User user) {
		
		UserResponseDto dto = new UserResponseDto();
		
		dto.setUserId(user.getUserId());
		dto.setFullName(user.getFullName());
		dto.setPhone(user.getPhone());
		
		return dto;
	}
}
