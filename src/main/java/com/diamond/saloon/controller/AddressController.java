package com.diamond.saloon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.common.ApiResponse;
import com.diamond.saloon.common.ApiResponseUtil;
import com.diamond.saloon.dto.AddressDto;
import com.diamond.saloon.service.AddressService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/addresses")
public class AddressController {

	@Autowired
	private AddressService addressService;

	// Add Address details
	@PostMapping
	public ResponseEntity<ApiResponse<AddressDto>> addAddress(
			@Valid @RequestBody AddressDto dto, HttpServletRequest request) {

		AddressDto saved = addressService.addAddress(dto);

		return ApiResponseUtil.success(
				saved,
				"Address added successfully",
				HttpStatus.CREATED,
				request
		);
	}

	// get all address
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<List<AddressDto>>> getAll(
			@PathVariable String userId, HttpServletRequest request) {

		List<AddressDto> addresses = addressService.getAll(userId);
		
		return ApiResponseUtil.success(
				addresses,
				"Addresses fetched successfully",
				HttpStatus.OK,
				request
		);
	}

	
	// Get address by Id
	@GetMapping("/id/{addressId}")
	public ResponseEntity<ApiResponse<AddressDto>> getById(
			@PathVariable String addressId, HttpServletRequest request) {

		AddressDto address = addressService.getById(addressId);
		
		return ApiResponseUtil.success(
				address,
				"Address fetched successfully",
				HttpStatus.OK,
				request
		);
	}

	
	// update address
	@PutMapping("/{addressId}")
	public ResponseEntity<ApiResponse<AddressDto>> updateAddress(
			@PathVariable String addressId, @Valid @RequestBody AddressDto dto, 
			HttpServletRequest request) {

		AddressDto updated = addressService.updateAddress(addressId, dto);
		
		return ApiResponseUtil.success(
				updated,
				"Address updated successfully",
				HttpStatus.OK,
				request
		);
	}

	
	// delete address
	@DeleteMapping("/{addressId}")
	public ResponseEntity<ApiResponse<Void>> deleteAddress(
			@PathVariable String addressId, HttpServletRequest request) {

		addressService.deleteAddress(addressId);
		
		return ApiResponseUtil.successMessage(
				"Address deleted successfully",
				HttpStatus.OK,
				request
		);
	}
	

	@GetMapping("/next-order/{userId}")
	public ResponseEntity<ApiResponse<AddressDto>> getAddressForNextOrder(
			@PathVariable String userId, HttpServletRequest request) {
		
		AddressDto address = addressService.getAddressForNextOrder(userId);
		
		return ApiResponseUtil.success(
				address,
				"Address fetched successfully",
				HttpStatus.OK,
				request
		);
	}

}
