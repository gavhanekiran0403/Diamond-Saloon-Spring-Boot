package com.diamond.saloon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.common.ApiResponse;
import com.diamond.saloon.common.ApiResponseUtil;
import com.diamond.saloon.dto.UserLoginDto;
import com.diamond.saloon.dto.AdminLoginDto;
import com.diamond.saloon.dto.UserDto;
import com.diamond.saloon.responsedto.UserResponseDto;
import com.diamond.saloon.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	// registered a new user
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserResponseDto>> register(
			@Valid @RequestBody UserDto user, HttpServletRequest request) {

		UserResponseDto response = authService.register(user);

		return ApiResponseUtil.success(
				response,
				"User registered successfully",
				HttpStatus.CREATED,
				request
		);
	}

	// Admin login
	@PostMapping("/admin/login")
	public ResponseEntity<ApiResponse<UserResponseDto>> adminLogin(
			@Valid @RequestBody AdminLoginDto dto, HttpServletRequest request) {

		UserResponseDto response = authService.adminLogin(dto);

		return ApiResponseUtil.success(
				response,
				"Admin login successful",
				HttpStatus.OK,
				request
		);
	}

	// User login
	@PostMapping("/user/login")
	public ResponseEntity<ApiResponse<UserResponseDto>> userLogin(
			@Valid @RequestBody UserLoginDto dto, HttpServletRequest request) {

		UserResponseDto response = authService.userLogin(dto);

		return ApiResponseUtil.success(
				response,
				"User login successful",
				HttpStatus.OK,
				request
		);
	}

	// logout user and admin
	@PostMapping("/logout/{userId}")
	public ResponseEntity<ApiResponse<Void>> logout(
			@PathVariable String userId, HttpServletRequest request) {

		authService.logout(userId);

		return ApiResponseUtil.successMessage(
				"Logout successful",
				HttpStatus.OK,
				request
		);
	}

}
