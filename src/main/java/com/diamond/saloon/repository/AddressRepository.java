package com.diamond.saloon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.model.Address;

@Repository
public interface AddressRepository extends MongoRepository<Address, String>{
	
	public List<Address> findByUserId(String userId);
	
	public Address findByUserIdAndDefaultAddressTrue(String userId);

}
