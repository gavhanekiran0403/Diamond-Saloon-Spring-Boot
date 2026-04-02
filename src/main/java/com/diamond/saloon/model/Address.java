package com.diamond.saloon.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "addresses")
@Data
public class Address {

	@Id
	private String addressId;

	@Indexed
	private String userId;

	private String fullName;

	private String phone;

	private String houseNo;

	private String streetAddress;

	private String city;

	private String state;

	private String pincode;

	private String landmark;

	@Indexed
	private boolean defaultAddress;

	private LocalDateTime createdAt;

}
