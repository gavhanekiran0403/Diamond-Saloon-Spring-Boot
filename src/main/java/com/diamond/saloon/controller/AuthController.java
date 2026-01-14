package com.diamond.saloon.controller;


import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.dto.AdminLoginDto;
import com.diamond.saloon.dto.LoginDto;
import com.diamond.saloon.dto.UserDto;
import com.diamond.saloon.responsedto.UserResponseDto;
import com.diamond.saloon.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	private AuthService authService;
	
	
	@PostMapping("/register")
	public UserResponseDto register(@Valid @RequestBody UserDto user) {
		return authService.register(user);
		
	}
	
	
	@PostMapping("/login")
	public UserResponseDto login(@Valid @RequestBody LoginDto dto) {
		return authService.login(dto);
	}
	
	@PostMapping("/admin/login")
	public UserResponseDto adminLogin(@Valid @RequestBody AdminLoginDto login) {
		return authService.adminLogin(login);
		
	}

	
	
	@PostMapping("/logout/{userId}")
	public String logout(@PathVariable String userId) {
		authService.logout(userId);
		return "Logout successful";
	}
	
	
}
