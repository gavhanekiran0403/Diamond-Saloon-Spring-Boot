package com.diamond.saloon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.diamond.saloon.model.ProductCategory;

@Repository
public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {

	public boolean existsByCategoryNameIgnoreCase(String categoryName);

	public Optional<ProductCategory> findByCategoryNameIgnoreCase(String categoryName);

	public List<ProductCategory> findByCategoryNameContainingIgnoreCaseOrderByCategoryNameAsc(String keyword);

}
