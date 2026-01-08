package com.diamond.saloon.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.LoginDto;
import com.diamond.saloon.dto.OtpVerifyDto;
import com.diamond.saloon.dto.UserDto;
import com.diamond.saloon.enums.Role;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.model.User;
import com.diamond.saloon.repository.UserRepository;
import com.diamond.saloon.responsedto.UserResponseDto;
import com.diamond.saloon.service.UserService;
import com.diamond.saloon.util.OtpUtil;

@Component
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public String register(UserDto userDto){
		
		userRepository.findByPhone(userDto.getPhone())
				.ifPresent(user -> {
					throw new BadRequestException("Mobile number already registerd");
				});
		
		User user = new User();
		user.setFullName(userDto.getFullName());
		user.setPhone(userDto.getPhone());
		user.setRole(Role.CUSTOMER);
		user.setLoginStatus(false);

		userRepository.save(user);
		
		return "User registered successfully";
	}

	@Override
	public String sendOTP(LoginDto dto) {
		User user = userRepository.findByPhone(dto.getPhone())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
				
		if(user.isLoginStatus()) {
			throw new BadRequestException("User already logged in on another device");
		}
		
		String otp = OtpUtil.generateOtp();
		System.out.println("OTP: "+ otp);
		
		user.setOtp(otp);
		user.setOtpExpiry(LocalDateTime.now().plusSeconds(60));
		
		userRepository.save(user);
		return "OTP sent successfully";
		
	}

	@Override
	public String verifyOTP(OtpVerifyDto dto) {
		User user = userRepository.findByPhone(dto.getPhone())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
				
		if(!user.getOtp().equals(dto.getOtp())) {
			throw new BadRequestException("Invalid OTP");
		}
		
		if(user.getOtpExpiry().isBefore(LocalDateTime.now())) {
			throw new BadRequestException("OTP expired");
		}
		
		user.setLoginStatus(true);
		user.setOtp(null);
		user.setOtpExpiry(null);
		
		userRepository.save(user);
		
		return "OTP verify successfully";

	}

	@Override
	public String resendOTP(String phone) {
		
		User user = userRepository.findByPhone(phone)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
				
		
		String otp = OtpUtil.generateOtp();
		System.out.println("OTP: "+ otp);
		
		user.setOtp(otp);
		user.setOtpExpiry(LocalDateTime.now().plusSeconds(60));
		
		userRepository.save(user);
		
		return "OTP resent successfully";
	}
	
	

	@Override
	public String logout(String phone) {

		User user = userRepository.findByPhone(phone)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		user.setLoginStatus(false);
		userRepository.save(user);
		
		return "Logout Successfully";
		
	}

	

}
