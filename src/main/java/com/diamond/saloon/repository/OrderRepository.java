package com.diamond.saloon.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.model.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

	public List<Order> findByUserId(String userId);

	public Order findTopByUserIdOrderByCreatedAtDesc(String userId);

	public List<Order> findByUserIdOrderByCreatedAtDesc(String userId);

	public Optional<Order> findByOrderIdAndUserId(String orderId, String userId);

	public List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}
