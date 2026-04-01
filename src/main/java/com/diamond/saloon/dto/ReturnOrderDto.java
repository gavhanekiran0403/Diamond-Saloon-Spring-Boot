package com.diamond.saloon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReturnOrderDto {

	@NotBlank(message = "User id is required")
	private String userId;

	@NotBlank(message = "Return reason is required")
	@Size(min = 5, max = 200, message = "Reason must be between 5 and 200 characters")
	private String reason;
}
