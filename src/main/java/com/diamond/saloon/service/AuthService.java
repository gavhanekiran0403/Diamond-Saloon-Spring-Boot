package com.diamond.saloon.service;

import com.diamond.saloon.dto.UserLoginDto;
import com.diamond.saloon.dto.AdminLoginDto;
import com.diamond.saloon.dto.UserDto;
import com.diamond.saloon.responsedto.UserResponseDto;

public interface AuthService {

	public UserResponseDto register(UserDto request);

	public UserResponseDto adminLogin(AdminLoginDto dto);

	public UserResponseDto userLogin(UserLoginDto dto);

	public void logout(String userId);

}
