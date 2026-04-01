package com.diamond.saloon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MergeCartDto {

	@NotBlank(message = "Guest user id is required")
	private String guestUserId;

	@NotBlank(message = "User id is required")
	private String userId;
}
