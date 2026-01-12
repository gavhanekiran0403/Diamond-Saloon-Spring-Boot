package com.diamond.saloon.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

	@NotBlank(message = "Name is required")
	private String fullName;
	
	@Email(message = "Invalid email format")
	private String email;
	
	
	
	@NotBlank(message = "Mobile number is required")
	@Pattern(
			regexp = "^[6-9][0-9]{9}$",
			message = "Invalid mobile number"
	)
	private String phone;
	
	
	@NotBlank(message = "password is required")
	@Size(min = 8, message = "password must be at least 8 characters")
	@Pattern(
			regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).*$",
	        message = "Password must contain uppercase, lowercase, number and special character"
	)
	private String password;
	
}
