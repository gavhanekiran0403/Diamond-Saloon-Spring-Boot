package com.diamond.saloon.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddressDto {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String addressId;

	@NotBlank(message = "User id is required")
	private String userId;

	@NotBlank(message = "Full name is required")
	private String fullName;

	@NotBlank(message = "Mobile number is required")
	@Pattern(
			regexp = "^[6-9][0-9]{9}$", 
			message = "Invalid mobile number"
		)
	private String phone;

	@NotBlank(message = "House number is required")
	private String houseNo;

	@NotBlank(message = "Street address is required")
	private String streetAddress;

	@NotBlank(message = "City is required")
	private String city;

	@NotBlank(message = "State is required")
	private String state;

	@NotBlank(message = "Pincode is required")
	@Pattern(
			regexp = "^[1-9][0-9]{5}$", 
			message = "Invalid pincode"
		)
	private String pincode;

	@NotBlank(message = "Landmark is required")
	private String landmark;

	private boolean defaultAddress;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy - hh:mma")
	@JsonProperty("timestamp")
	private LocalDateTime createdAt;

}
