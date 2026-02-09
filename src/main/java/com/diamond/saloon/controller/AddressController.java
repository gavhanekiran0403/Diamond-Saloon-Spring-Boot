package com.diamond.saloon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.dto.AddressDto;
import com.diamond.saloon.service.AddressService;



@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	private AddressService addressService;
	
	
	// Add Address details
	@PostMapping("/add")
	public AddressDto addAddress(@RequestBody AddressDto dto) {
		return addressService.addAddress(dto);
	}
	
	
	// get all address
	@GetMapping("/get-all/{userId}")
	public List<AddressDto> getAll(@PathVariable String userId){
		return addressService.getAll(userId);
	}
	
	
	// Auto set default address
	@PutMapping("/default/{addressId}")
	public AddressDto setDefault(@PathVariable String addressId){
		return addressService.setDefaultAddress(addressId);
	}
	
	
	//update address
	@PutMapping("/update/{addressId}")
	public AddressDto update(@PathVariable String addressId, @RequestBody AddressDto dto) {
		return addressService.updateAddress(addressId, dto);
	}
	
	
	//delete address
	@DeleteMapping("/delete/{addressId}")
	public String deleteAddress(@PathVariable String addressId) {
		addressService.deleteAddress(addressId);
		return "Address deleted successfully";
	}
	
}
