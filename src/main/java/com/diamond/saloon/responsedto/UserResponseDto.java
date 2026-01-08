package com.diamond.saloon.responsedto;

import com.diamond.saloon.enums.Role;

import lombok.Data;

@Data
public class UserResponseDto {

	private String userId;
	private String fullName;
	private String phone;

}
