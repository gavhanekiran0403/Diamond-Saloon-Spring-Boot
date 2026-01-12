package com.diamond.saloon.serviceimpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.UserDto;
import com.diamond.saloon.enums.Role;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.UserMapper;
import com.diamond.saloon.model.User;
import com.diamond.saloon.repository.UserRepository;
import com.diamond.saloon.responsedto.UserResponseDto;
import com.diamond.saloon.service.UserService;

@Component
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<UserResponseDto> getAllUsers() {
		List<User> users = userRepository.findAll();
		
		if(users.isEmpty()) {
			throw new ResourceNotFoundException("No users found");
		}
		
		return users.stream()
				.filter(user -> user.getRole()==Role.CUSTOMER)
				.map(UserMapper :: toDto)
				.toList();
	}


	@Override
	public UserResponseDto updateUser(String userId, UserDto update) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		user.setFullName(update.getFullName());
		user.setEmail(update.getEmail());
		userRepository.save(user);
		return UserMapper.toDto(user);
	}


	@Override
	public UserResponseDto getUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		return UserMapper.toDto(user);
	}
}
