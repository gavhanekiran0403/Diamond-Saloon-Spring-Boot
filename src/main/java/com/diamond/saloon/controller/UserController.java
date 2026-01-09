package com.diamond.saloon.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.dto.LoginDto;
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
	public UserResponseDto register(@Valid @RequestBody UserDto user) {
		return userService.register(user);
		
	}
	
	
	@PostMapping("/login")
	public UserResponseDto login(@Valid @RequestBody LoginDto dto) {
		return userService.login(dto);
	}
	
	
	@PostMapping("/logout/{userId}")
	public String logout(@PathVariable String userId) {
		userService.logout(userId);
		return "Logout successful";
	}
	
	
}
