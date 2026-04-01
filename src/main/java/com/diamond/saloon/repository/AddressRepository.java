package com.diamond.saloon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.model.Address;

@Repository
public interface AddressRepository extends MongoRepository<Address, String> {

	public List<Address> findByUserId(String userId, Sort sort);

	Optional<Address> findByAddressIdAndUserId(String addressId, String userId);

	boolean existsByUserIdAndHouseNoIgnoreCaseAndStreetAddressIgnoreCaseAndCityIgnoreCaseAndStateIgnoreCaseAndPincode(
			String userId, String houseNo, String streetAddress, String city, String state, String pincode);

}