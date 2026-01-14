package com.diamond.saloon.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.ProductCategoryDto;
import com.diamond.saloon.model.ProductCategory;


@Service
public interface ProductCategoryService {

	public ProductCategory addCategory(ProductCategoryDto categoryDto);
	
	public List<ProductCategory> getAllCategories();
	
	public ProductCategory getProductCategoryById(String categoryId);
	
	public ProductCategory updateProductCategory(String productCategoryId, ProductCategoryDto categoryDto);
	
	public void deleteProductCategory(String categoryId);
	
	
}
