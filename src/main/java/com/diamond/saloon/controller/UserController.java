package com.diamond.saloon.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.dto.LoginDto;
import com.diamond.saloon.dto.OtpVerifyDto;
import com.diamond.saloon.dto.UserDto;
import com.diamond.saloon.responsedto.UserResponseDto;
import com.diamond.saloon.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@PostMapping("/register")
	public String register(@Valid @RequestBody UserDto user) {
		return userService.register(user);
		
	}
	
	@PostMapping("/login/send-otp")
	public String sendOTP(@RequestBody LoginDto dto) {
		return userService.sendOTP(dto);
	}
	
	
	@PostMapping("/login/verify-otp")
	public String verifyOTP(@RequestBody OtpVerifyDto dto) {
		return userService.verifyOTP(dto);
	}
	
	@PostMapping("/login/resend-otp/{phone}")
	public void resendOTP(@PathVariable String phone) {
		userService.resendOTP(phone);
	}
	
	@PostMapping("/logout/{phone}")
	public void logout(@PathVariable String phone) {
		userService.logout(phone);
	}
	
	
}
