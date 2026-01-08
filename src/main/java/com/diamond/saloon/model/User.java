package com.diamond.saloon.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.diamond.saloon.enums.Role;

import lombok.Data;

@Document(collection = "users")
@Data
public class User {

	@Id
	private String userId;
	
	private String fullName;
	private String phone;
	
	private Role role;
	
	private String otp;
	private LocalDateTime otpExpiry;
	
	private boolean loginStatus;
	
}
