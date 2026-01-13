package com.diamond.saloon.serviceimpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.ProductCategoryDto;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.ProductCategoryMapper;
import com.diamond.saloon.model.ProductCategory;
import com.diamond.saloon.repository.ProductCategoryRepository;
import com.diamond.saloon.service.ProductCategoryService;

@Component
public class ProductCategoryServiceImpl implements ProductCategoryService{
	
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	
	

	@Override
	public ProductCategory addCategory(ProductCategoryDto categoryDto) {

		if(productCategoryRepository.existsByCategoryName(categoryDto.getCategoryName())) {
			throw new BadRequestException("Product category already exists");
		}
		
		ProductCategory category = ProductCategoryMapper.toEntity(categoryDto);
		
		return productCategoryRepository.save(category);
		
			
	}
	
	

	@Override
	public List<ProductCategory> getAllCategories() {

		return productCategoryRepository.findAll();
	}
	
	

	@Override
	public ProductCategory getProductCategoryById(String categoryId) {
		
		return productCategoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Product category not found"));
	}
	
	

	@Override
	public ProductCategory updateProductCategory(String productCategoryId, ProductCategoryDto categoryDto) {

		ProductCategory category = getProductCategoryById(productCategoryId);
		
		ProductCategoryMapper.updateEntity(category, categoryDto);
				
		
		return productCategoryRepository.save(category);
	}



	@Override
	public void deleteProductCategory(String categroyId) {
		ProductCategory category = getProductCategoryById(categroyId);
		productCategoryRepository.delete(category);
		
	}

	

	

}
