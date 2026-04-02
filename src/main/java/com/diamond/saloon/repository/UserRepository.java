package com.diamond.saloon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.enums.Role;
import com.diamond.saloon.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	public Optional<User> findByPhone(String phone);

	public boolean existsByPhone(String phone);

	public Optional<User> findByEmail(String email);

	public boolean existsByEmail(String email);

	public List<User> findByRole(Role role);
}
