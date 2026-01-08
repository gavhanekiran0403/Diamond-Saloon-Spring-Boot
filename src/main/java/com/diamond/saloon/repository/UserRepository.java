package com.diamond.saloon.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{

	public Optional<User> findByPhone(String phone);
	
}
