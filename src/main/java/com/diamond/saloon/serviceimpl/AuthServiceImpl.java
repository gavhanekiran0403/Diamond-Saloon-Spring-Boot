package com.diamond.saloon.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.AdminLoginDto;
import com.diamond.saloon.dto.LoginDto;
import com.diamond.saloon.dto.UserDto;
import com.diamond.saloon.enums.Role;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.mapper.UserMapper;
import com.diamond.saloon.model.User;
import com.diamond.saloon.repository.UserRepository;
import com.diamond.saloon.responsedto.UserResponseDto;
import com.diamond.saloon.service.AuthService;


@Component
public class AuthServiceImpl implements AuthService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	public UserResponseDto register(UserDto request){
		
		if(userRepository.existsByPhone(request.getPhone())) {
			throw new RuntimeException("Mobile number already registered");
		}
		
		
		User user = new User();
		user.setFullName(request.getFullName());
		user.setPhone(request.getPhone());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setRole(Role.CUSTOMER);
		user.setLoginStatus(false);

		
		
		return UserMapper.toDto(userRepository.save(user));
	}


	@Override
	public UserResponseDto login(LoginDto dto) {
		
		User user = userRepository.findByPhone(dto.getPhone())
				.orElseThrow(() -> new BadRequestException("Invalid mobile number"));
		
		if(!user.getPassword().equals(dto.getPassword())) {
			throw new BadRequestException("Invalid password");
		}
		
		if(Boolean.TRUE.equals(user.getLoginStatus())) {
			throw new BadRequestException("User already logged in on another device");
		}
		
		
		user.setLoginStatus(true);
		
		return UserMapper.toDto(userRepository.save(user));
	}
	
	
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


	@Override
	public void logout(String userId) {
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BadRequestException("User not found"));
		
		user.setLoginStatus(false);
		userRepository.save(user);
		
	}
}
