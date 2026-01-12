package com.diamond.saloon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class AdminLoginDto {

	@NotBlank(message = "Email is required")
	private String email;
	
	@NotBlank(message = "Password is required")
	private String password;
}
