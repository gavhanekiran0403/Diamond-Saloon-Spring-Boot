package com.diamond.saloon.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.AddressDto;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.AddressMapper;
import com.diamond.saloon.model.Address;
import com.diamond.saloon.repository.AddressRepository;
import com.diamond.saloon.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService{
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private AddressMapper addressMapper;

	@Override
	public AddressDto addAddress(AddressDto dto) {
		
		if(dto.isDefaultAddress()) {
			Address existing = addressRepository.findByUserIdAndDefaultAddressTrue(dto.getUserId());
			
			if(existing != null) {
				existing.setDefaultAddress(false);
				addressRepository.save(existing);
			}
		}
		
		Address address = addressMapper.toEntity(dto);
		
		return addressMapper.toDto(addressRepository.save(address));
	}

	@Override
	public List<AddressDto> getAll(String userId) {
		
		return addressRepository.findByUserId(userId)
				.stream()
				.map(addressMapper :: toDto)
				.toList();
	}

	@Override
	public AddressDto setDefaultAddress(String addressId) {
		
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found"));
		
		Address existing = addressRepository.findByUserIdAndDefaultAddressTrue(address.getUserId());
		
		if(existing != null) {
			existing.setDefaultAddress(false);
			addressRepository.save(existing);
		}
		address.setDefaultAddress(true);
		
		return addressMapper.toDto(addressRepository.save(address));
	}

	@Override
	public AddressDto updateAddress(String addressId, AddressDto dto) {
		
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found"));
		
		address.setFullName(dto.getFullName());
		address.setPhone(dto.getPhone());
		address.setHouseNo(dto.getHouseNo());
		address.setStreetAddress(dto.getStreetAddress());
		address.setCity(dto.getCity());
		address.setState(dto.getState());
		address.setPincode(dto.getPincode());
		address.setLandmark(dto.getLandmark());
		
		return addressMapper.toDto(addressRepository.save(address));
	}

	@Override
	public void deleteAddress(String addressId) {
		
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found"));
		
		addressRepository.delete(address);
		
	}

}
