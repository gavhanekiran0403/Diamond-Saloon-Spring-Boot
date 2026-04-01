package com.diamond.saloon.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

	public List<Product> findByProductCategoryId(String categoryId);

	public boolean existsByProductCategoryId(String categoryId);
	
	public boolean existsByProductNameIgnoreCaseAndBrandIgnoreCaseAndProductCategoryId(
	        String productName,
	        String brand,
	        String productCategoryId
	);
	
	public boolean existsByProductNameIgnoreCaseAndBrandIgnoreCaseAndProductCategoryIdAndProductIdNot(
			String productName,
	        String brand,
	        String productCategoryId,
	        String productId);

	public List<Product> findByProductIdIn(List<String> productIds);

}
