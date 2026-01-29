package com.diamond.saloon.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.model.Staff;

@Repository
public interface StaffRepository extends MongoRepository<Staff, String> {

    List<Staff> findBySpecialization(String specialization);

    List<Staff> findByIsAvailable(boolean isAvailable);
}
