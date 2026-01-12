package com.diamond.saloon.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.model.Service;

@Repository
public interface ServiceRepository extends MongoRepository<Service, String> {

    // Find services by MEN / WOMEN category
    List<Service> findByCategory(String category);

    // Find all active services
    List<Service> findByIsActive(boolean isActive);
}
