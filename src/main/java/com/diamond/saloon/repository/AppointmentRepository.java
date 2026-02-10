package com.diamond.saloon.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.model.Appointment;

@Repository
public interface AppointmentRepository 
        extends MongoRepository<Appointment, String> {

    List<Appointment> findByUserId(String userId);
    List<Appointment> findByStatus(String status);
}
