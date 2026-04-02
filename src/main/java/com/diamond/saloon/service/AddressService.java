package com.diamond.saloon.service;

import java.util.List;

import com.diamond.saloon.dto.AddressDto;

public interface AddressService {

	public AddressDto addAddress(AddressDto dto);

	public List<AddressDto> getAll(String userId);

	public AddressDto updateAddress(String addressId, AddressDto dto);

	public void deleteAddress(String addressId);

	public AddressDto getById(String addressId);

	public AddressDto getAddressForNextOrder(String userId);

}
