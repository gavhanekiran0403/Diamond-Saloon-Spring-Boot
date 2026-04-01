package com.diamond.saloon.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDto {

	   @Size(min = 2, max = 50)
	   @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only letters")
	   private String fullName;

	   @Email(message = "Invalid email format")
	   @Size(max = 100, message = "Email must not exceed 100 characters")
	   private String email;
	
}
