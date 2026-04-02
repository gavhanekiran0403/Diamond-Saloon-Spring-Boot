package com.diamond.saloon.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.AddressDto;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.AddressMapper;
import com.diamond.saloon.model.Address;
import com.diamond.saloon.model.Order;
import com.diamond.saloon.model.OrderAddress;
import com.diamond.saloon.repository.AddressRepository;
import com.diamond.saloon.repository.OrderRepository;
import com.diamond.saloon.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private AddressMapper addressMapper;

	@Autowired
	private OrderRepository orderRepository;
	

	@Override
	public AddressDto addAddress(AddressDto dto) {

		if (dto.getUserId() == null || dto.getUserId().isBlank()) {
			throw new BadRequestException("User id is required");
		}

		Address address = addressMapper.toEntity(dto);
		address.setAddressId(null);
		normalizeAddress(address);

		validateDuplicate(address);

		if (dto.isDefaultAddress()) {

			List<Address> addresses = addressRepository.findByUserId(dto.getUserId(), Sort.unsorted());

			for (Address addr : addresses) {
				addr.setDefaultAddress(false);
			}

			addressRepository.saveAll(addresses);
		}
		address.setCreatedAt(LocalDateTime.now());

		return addressMapper.toDto(addressRepository.save(address));
	}
	

	@Override
	public List<AddressDto> getAll(String userId) {
		return addressRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "createdAt"))
				.stream()
				.map(addressMapper::toDto)
				.toList();
	}

	
	@Override
	public AddressDto getById(String addressId) {
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));
		return addressMapper.toDto(address);
	}

	
	@Override
	public AddressDto updateAddress(String addressId, AddressDto dto) {
		Address existing = addressRepository
			    .findByAddressIdAndUserId(addressId, dto.getUserId())
			    .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

		normalizeAddress(dto);
		existing.setFullName(dto.getFullName());
		existing.setPhone(dto.getPhone());
		existing.setHouseNo(dto.getHouseNo());
		existing.setStreetAddress(dto.getStreetAddress());
		existing.setCity(dto.getCity());
		existing.setState(dto.getState());
		existing.setPincode(dto.getPincode());
		existing.setLandmark(dto.getLandmark());

		validateDuplicate(existing);

		if (dto.isDefaultAddress()) {

			List<Address> addresses = addressRepository.findByUserId(existing.getUserId(), Sort.unsorted());

			for (Address addr : addresses) {
				if (!addr.getAddressId().equals(addressId)) {
					addr.setDefaultAddress(false);
				}
			}

			addressRepository.saveAll(addresses);
		}

		return addressMapper.toDto(addressRepository.save(existing));
	}

	
	@Override
	public void deleteAddress(String addressId) {
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

		boolean wasDefault = address.isDefaultAddress();
		String userId = address.getUserId();

		addressRepository.delete(address);

		if (wasDefault) {

			List<Address> addresses = addressRepository.findByUserId(userId, Sort.unsorted());

			if (!addresses.isEmpty()) {
				Address newDefault = addresses.get(0);
				newDefault.setDefaultAddress(true);
				addressRepository.save(newDefault);
			}
		}
	}

	@Override
	public AddressDto getAddressForNextOrder(String userId) {

	    Order lastOrder = orderRepository.findTopByUserIdOrderByCreatedAtDesc(userId);

	    if (lastOrder != null && lastOrder.getDeliveryAddress() != null) {
	        return convertOrderAddressToDto(lastOrder.getDeliveryAddress(), userId);
	    }

	    // check default address
	    List<Address> addresses = addressRepository.findByUserId(userId, Sort.unsorted());

	    return addresses.stream()
	            .filter(Address::isDefaultAddress)
	            .findFirst()
	            .map(addressMapper::toDto)
	            .orElseGet(() -> addresses.stream()
	                    .findFirst()
	                    .map(addressMapper::toDto)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException("No address found for userId: " + userId)));
	}
	

	// Utility methods
	// convert order address to Dto for next Order
	private AddressDto convertOrderAddressToDto(OrderAddress orderAddress, String userId) {

		AddressDto dto = new AddressDto();

		dto.setUserId(userId);
		dto.setFullName(orderAddress.getFullName());
		dto.setPhone(orderAddress.getPhone());
		dto.setHouseNo(orderAddress.getHouseNo());
		dto.setStreetAddress(orderAddress.getStreetAddress());
		dto.setCity(orderAddress.getCity());
		dto.setState(orderAddress.getState());
		dto.setPincode(orderAddress.getPincode());
		dto.setLandmark(orderAddress.getLandmark());

		return dto;
	}
	

	// Address normalization method
	private String normalize(String value) {
		return value == null ? null : value.trim();
	}

	private void normalizeAddress(Address address) {
		address.setFullName(normalize(address.getFullName()));
		address.setPhone(normalize(address.getPhone()));
		address.setHouseNo(normalize(address.getHouseNo()));
		address.setStreetAddress(normalize(address.getStreetAddress()));
		address.setCity(normalize(address.getCity()));
		address.setState(normalize(address.getState()));
		address.setPincode(normalize(address.getPincode()));
		address.setLandmark(normalize(address.getLandmark()));
	}

	private void normalizeAddress(AddressDto dto) {
		dto.setFullName(normalize(dto.getFullName()));
		dto.setPhone(normalize(dto.getPhone()));
		dto.setHouseNo(normalize(dto.getHouseNo()));
		dto.setStreetAddress(normalize(dto.getStreetAddress()));
		dto.setCity(normalize(dto.getCity()));
		dto.setState(normalize(dto.getState()));
		dto.setPincode(normalize(dto.getPincode()));
		dto.setLandmark(normalize(dto.getLandmark()));
	}

	
	// Duplicate Address validation method
	private void validateDuplicate(Address address) {

	    boolean exists = addressRepository
	        .existsByUserIdAndHouseNoIgnoreCaseAndStreetAddressIgnoreCaseAndCityIgnoreCaseAndStateIgnoreCaseAndPincode(
	            address.getUserId(),
	            address.getHouseNo(),
	            address.getStreetAddress(),
	            address.getCity(),
	            address.getState(),
	            address.getPincode()
	        );

	    if (exists && address.getAddressId() == null) {
	        throw new BadRequestException("Duplicate address already exists");
	    }

	    if (exists && address.getAddressId() != null) {

	        Address existing = addressRepository
	            .findByAddressIdAndUserId(address.getAddressId(), address.getUserId())
	            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

	        boolean isSame =
	                existing.getHouseNo().equalsIgnoreCase(address.getHouseNo()) &&
	                existing.getStreetAddress().equalsIgnoreCase(address.getStreetAddress()) &&
	                existing.getCity().equalsIgnoreCase(address.getCity()) &&
	                existing.getState().equalsIgnoreCase(address.getState()) &&
	                existing.getPincode().equals(address.getPincode());

	        if (!isSame) {
	            throw new BadRequestException("Duplicate address already exists");
	        }
	    }
	}

}