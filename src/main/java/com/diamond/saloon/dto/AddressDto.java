package com.diamond.saloon.dto;

import lombok.Data;

@Data
public class AddressDto {

	private String addressId;
	
	private String userId;
	
	private String fullName;
	
	private String phone;
	
	private String houseNo;
	
	private String streetAddress;
	
	private String city;
	
	private String state;
	
	private String pincode;
	 
	private String landmark;
	 
	private boolean defaultAddress;
	
}
