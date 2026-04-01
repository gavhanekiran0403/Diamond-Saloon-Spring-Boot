package com.diamond.saloon.service;

import java.util.List;  

import com.diamond.saloon.dto.ChangePasswordDto;
import com.diamond.saloon.dto.UserUpdateDto;
import com.diamond.saloon.responsedto.UserResponseDto;

public interface UserService {

	public UserResponseDto getUser(String userId);
	
	public List<UserResponseDto> getAllUsers();
	
	public UserResponseDto updateUser(String userId, UserUpdateDto update);
	
	public void changePassword(String userId, ChangePasswordDto passwordDto);
}
