package com.diamond.saloon.service;

import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.AdminLoginDto;
import com.diamond.saloon.responsedto.UserResponseDto;

@Service
public interface AdminService {

	public UserResponseDto adminLogin(AdminLoginDto adminDto);
}
