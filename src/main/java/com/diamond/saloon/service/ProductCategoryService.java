package com.diamond.saloon.service;

import java.util.List;


import com.diamond.saloon.dto.ProductCategoryDto;




public interface ProductCategoryService {

	public ProductCategoryDto addCategory(ProductCategoryDto categoryDto);
	
	public List<ProductCategoryDto> getAllCategories();
	
	public ProductCategoryDto getProductCategoryById(String categoryId);
	
	public ProductCategoryDto updateProductCategory(String productCategoryId, ProductCategoryDto categoryDto);
	
	public void deleteProductCategory(String categoryId);
	
	
}
