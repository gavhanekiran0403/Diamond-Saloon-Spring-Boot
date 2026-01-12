package com.diamond.saloon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.dto.AdminLoginDto;
import com.diamond.saloon.dto.UserDto;
import com.diamond.saloon.model.User;
import com.diamond.saloon.responsedto.UserResponseDto;
import com.diamond.saloon.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@GetMapping("/{userId}")
	public UserResponseDto getUser(@PathVariable String userId) {
		return userService.getUser(userId);
		
	}
	
	@GetMapping("/get-all")
	public List<UserResponseDto> getAllUsers(){
		return userService.getAllUsers();
	}
	
	@PutMapping("/update/{userId}")
	public UserResponseDto updateUser(@PathVariable String userId, @RequestBody UserDto update) {
		return userService.updateUser(userId, update);
	}
}
