package com.diamond.saloon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginDto {

	@NotBlank(message = "Mobile number is required")
	@Pattern(
			regexp = "^[6-9][0-9]{9}$",
			message = "Invalid mobile number"
	)
	private String phone;
	
	@NotBlank(message = "Password is required")
	private String password;
}
