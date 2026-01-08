package com.diamond.saloon.service;

import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.LoginDto;
import com.diamond.saloon.dto.OtpVerifyDto;
import com.diamond.saloon.dto.UserDto;


@Service
public interface UserService {

	public String register(UserDto user);
	
	public String sendOTP(LoginDto dto);
	
	public String verifyOTP(OtpVerifyDto dto);
	
	public String resendOTP(String phone);
	
	public String logout(String phone);
}
