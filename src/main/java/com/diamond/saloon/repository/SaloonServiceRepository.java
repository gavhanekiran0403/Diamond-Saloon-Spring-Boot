package com.diamond.saloon.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.model.SaloonServiceEntity;

@Repository
public interface SaloonServiceRepository
        extends MongoRepository<SaloonServiceEntity, String> {

    List<SaloonServiceEntity> findByCategory(String category);

    List<SaloonServiceEntity> findByIsActive(boolean isActive);
}
