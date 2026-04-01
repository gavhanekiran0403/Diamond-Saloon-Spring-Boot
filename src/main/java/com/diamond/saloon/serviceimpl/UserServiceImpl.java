package com.diamond.saloon.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.ChangePasswordDto;
import com.diamond.saloon.dto.UserUpdateDto;
import com.diamond.saloon.enums.Role;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.UserMapper;
import com.diamond.saloon.model.User;
import com.diamond.saloon.repository.UserRepository;
import com.diamond.saloon.responsedto.UserResponseDto;
import com.diamond.saloon.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	
	@Override
	public UserResponseDto getUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		return userMapper.toDto(user);
	}

	
	@Override
	public List<UserResponseDto> getAllUsers() {

		List<User> users = userRepository.findByRole(Role.CUSTOMER);

		return users.stream()
				.map(userMapper::toDto)
				.toList();
	}
	

	@Override
	public UserResponseDto updateUser(String userId, UserUpdateDto update) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		boolean updated = false;

		if (update.getFullName() != null && !update.getFullName().isBlank()) {
			user.setFullName(update.getFullName().trim());
			updated = true;
		}

		if (update.getEmail() != null && !update.getEmail().isBlank()) {

			String newEmail = update.getEmail().toLowerCase();

			if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
				throw new BadRequestException("Email already registered by another user");
			}

			user.setEmail(newEmail);
			updated = true;
		}

		if (!updated) {
			throw new BadRequestException("No fields provided to update");
		}
		return userMapper.toDto(userRepository.save(user));
	}

	
	@Override
	public void changePassword(String userId, ChangePasswordDto passwordDto) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		if(!user.getLoginStatus()){
	        throw new BadRequestException("User must be logged in");
	    }

		if (!user.getPassword().equals(passwordDto.getOldPassword())) {
			throw new BadRequestException("Old password does not match");
		}

		if (user.getPassword().equals(passwordDto.getNewPassword())) {
			throw new BadRequestException("New password cannot be same as old password");
		}

		if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
	        throw new BadRequestException("Passwords do not match");
	    }
		
		user.setPassword(passwordDto.getNewPassword().trim());

		userRepository.save(user);

	}

}
