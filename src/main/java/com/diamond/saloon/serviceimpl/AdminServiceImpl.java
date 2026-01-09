package com.diamond.saloon.serviceimpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.AdminLoginDto;
import com.diamond.saloon.enums.Role;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.mapper.UserMapper;
import com.diamond.saloon.model.User;
import com.diamond.saloon.repository.UserRepository;
import com.diamond.saloon.responsedto.UserResponseDto;
import com.diamond.saloon.service.AdminService;

@Component
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserResponseDto adminLogin(AdminLoginDto adminDto) {
		
		User admin = userRepository.findByEmail(adminDto.getEmail())
				.orElseThrow(() -> new BadRequestException("Invalid email"));
		
		if(admin.getRole() != Role.ADMIN) {
			throw new BadRequestException("Access denied. Not an admin");
		}
		
		if(!admin.getPassword().equals(adminDto.getPassword())) {
			throw new BadRequestException("Invalid password");
		}
		
		
		
		
		return UserMapper.toDto(admin);
	}

}
