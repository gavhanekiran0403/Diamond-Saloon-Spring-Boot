package com.diamond.saloon.repository;

import java.util.List; 

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String>{

	public List<Product> findByProductCategoryId(String categoryId);
}
