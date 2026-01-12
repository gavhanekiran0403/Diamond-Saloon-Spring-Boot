package com.diamond.saloon.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.diamond.saloon.enums.Role;

import lombok.Data;

@Document(collection = "users")
@Data
public class User {

	@Id
	private String userId;
	
	private String fullName;
	private String email;
	
	@Indexed(unique = true)
	private String phone;
	private String password;
	private Role role;
	
	private Boolean loginStatus = false;
	
}
