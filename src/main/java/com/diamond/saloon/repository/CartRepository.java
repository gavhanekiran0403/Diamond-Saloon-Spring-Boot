package com.diamond.saloon.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.model.Cart;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {

	Optional<Cart> findByUserId(String userId);
}
