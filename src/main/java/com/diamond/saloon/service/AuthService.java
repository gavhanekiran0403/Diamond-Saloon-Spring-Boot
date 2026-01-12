package com.diamond.saloon.service;

import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.AdminLoginDto;
import com.diamond.saloon.dto.LoginDto;
import com.diamond.saloon.dto.UserDto;
import com.diamond.saloon.responsedto.UserResponseDto;



@Service
public interface AuthService {

	public UserResponseDto register(UserDto request);
	
	public UserResponseDto login(LoginDto dto);
	
	public UserResponseDto adminLogin(AdminLoginDto adminDto);
	
	public void logout(String userId);
	
}
