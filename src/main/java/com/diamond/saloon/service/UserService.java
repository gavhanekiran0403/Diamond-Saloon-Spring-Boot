package com.diamond.saloon.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.AdminLoginDto;
import com.diamond.saloon.dto.RegisterDto;
import com.diamond.saloon.responsedto.UserResponseDto;

@Service
public interface UserService {

	public UserResponseDto getUser(String userId);
	
	public List<UserResponseDto> getAllUsers();
}
