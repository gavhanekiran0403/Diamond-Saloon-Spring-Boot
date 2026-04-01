package com.diamond.saloon.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired; 

import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.UserLoginDto;
import com.diamond.saloon.dto.AdminLoginDto;
import com.diamond.saloon.dto.UserDto;
import com.diamond.saloon.enums.Role;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.mapper.UserMapper;
import com.diamond.saloon.model.User;
import com.diamond.saloon.repository.UserRepository;
import com.diamond.saloon.responsedto.UserResponseDto;
import com.diamond.saloon.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserResponseDto register(UserDto request) {

		if (userRepository.existsByPhone(request.getPhone())) {
			throw new BadRequestException("Mobile number already registered");
		}

		User user = new User();
		user.setFullName(request.getFullName() .trim());
		user.setPhone(request.getPhone().trim());

		if (request.getEmail() != null && !request.getEmail().isBlank()) {
			String emailLower = request.getEmail().toLowerCase();
			if (userRepository.existsByEmail(emailLower)) {
				throw new BadRequestException("Email already registered by another user");
			}
			user.setEmail(emailLower);
		}

		user.setPassword(request.getPassword());
		user.setRole(Role.CUSTOMER);
		user.setLoginStatus(false);

		return userMapper.toDto(userRepository.save(user));
	}

	
	@Override
	public UserResponseDto adminLogin(AdminLoginDto dto) {

		User user = userRepository.findByEmail(dto.getEmail().toLowerCase())
				.orElseThrow(() -> new BadRequestException("Invalid email"));

		if (user.getRole() != Role.ADMIN) {
			throw new BadRequestException("Access denied. Not an admin account");
		}

		if (!user.getPassword().equals(dto.getPassword())) {
			throw new BadRequestException("Invalid password");
		}

		user.setLoginStatus(true);

		return userMapper.toDto(userRepository.save(user));
	}
	

	@Override
	public UserResponseDto userLogin(UserLoginDto dto) {

		User user = userRepository.findByPhone(dto.getPhone())
				.orElseThrow(() -> new BadRequestException("Invalid mobile number"));

		if (user.getRole() != Role.CUSTOMER) {
			throw new BadRequestException("Access denied. Not a customer account");
		}

		if (!user.getPassword().equals(dto.getPassword())) {
			throw new BadRequestException("Invalid password");
		}

		user.setLoginStatus(true);

		return userMapper.toDto(userRepository.save(user));
	}
	

	@Override
	public void logout(String userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BadRequestException("User not found"));

		user.setLoginStatus(false);
		userRepository.save(user);

	}

}
