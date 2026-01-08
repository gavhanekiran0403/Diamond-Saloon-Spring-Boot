package com.diamond.saloon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OtpVerifyDto {

	@NotBlank(message = "Mobile number is required")
	@Pattern(
			regexp = "^[6-9][0-9]{9}$",
			message = "Invalid mobile number"
	)
	private String phone;
	
	@NotBlank(message = "OTP is required")
	@Pattern(
		regexp = "\\d{6}",
		message = "OTP must be 6 digits"
			)
	private String otp;
	
}
