package com.diamond.saloon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.dto.AdminLoginDto;
import com.diamond.saloon.responsedto.UserResponseDto;
import com.diamond.saloon.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@PostMapping("/login")
	public UserResponseDto adminLogin(@Valid @RequestBody AdminLoginDto login) {
		return adminService.adminLogin(login);
		
	}
	
}
