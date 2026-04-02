package com.diamond.saloon.controller;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.common.ApiResponse;
import com.diamond.saloon.common.ApiResponseUtil;
import com.diamond.saloon.dto.ChangePasswordDto;
import com.diamond.saloon.dto.UserUpdateDto;
import com.diamond.saloon.responsedto.UserResponseDto;
import com.diamond.saloon.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	
	// get user profile
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserResponseDto>> getUser(
			@PathVariable String userId, HttpServletRequest request) {

		UserResponseDto response = userService.getUser(userId);

		return ApiResponseUtil.success(
				response,
				"User fetched successfully",
				HttpStatus.OK,
				request
		);
	}

	// get all user (admin api)
	@GetMapping
	public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers(HttpServletRequest request) {

		List<UserResponseDto> users = userService.getAllUsers();

		return ApiResponseUtil.success(
				users,
				"Users fetched successfully",
				HttpStatus.OK,
				request
		);
	}

	// update user profile
	@PutMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@PathVariable String userId,
			@Valid @RequestBody UserUpdateDto update, HttpServletRequest request) {

		UserResponseDto response = userService.updateUser(userId, update);

		return ApiResponseUtil.success(
				response,
				"User updated successfully",
				HttpStatus.OK,
				request
		);
	}

	// change password
	@PutMapping("/{userId}/change-password")
	public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable String userId,
			@Valid @RequestBody ChangePasswordDto passwordDto, HttpServletRequest request) {

		userService.changePassword(userId, passwordDto);

		return ApiResponseUtil.successMessage(
				"Password changed successfully",
				HttpStatus.OK,
				request
		);
	}
}
