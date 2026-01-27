package com.diamond.saloon.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.model.SaloonPackageEntity;

@Repository
public interface SaloonPackageRepository
        extends MongoRepository<SaloonPackageEntity, String> {

    List<SaloonPackageEntity> findByCategory(String category);
}
