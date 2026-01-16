package com.diamond.saloon.serviceimpl;


import java.util.List; 

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.ProductCategoryDto;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.ProductCategoryMapper;
import com.diamond.saloon.model.ProductCategory;
import com.diamond.saloon.repository.ProductCategoryRepository;
import com.diamond.saloon.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
	
	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	
	@Autowired
	private ProductCategoryMapper productCategoryMapper;

	@Override
	public ProductCategoryDto addCategory(ProductCategoryDto categoryDto) {

		if(productCategoryRepository.existsByCategoryName(categoryDto.getCategoryName())) {
			throw new BadRequestException("Product category already exists");
		}
		
		ProductCategory category = productCategoryMapper.toEntity(categoryDto);
		
		return productCategoryMapper.toDto(productCategoryRepository.save(category));
		
			
	}
	
	

	@Override
	public List<ProductCategoryDto> getAllCategories() {

		return productCategoryRepository.findAll()
				.stream()
				.map(productCategoryMapper:: toDto)
				.toList();
	}
	
	

	@Override
	public ProductCategoryDto getProductCategoryById(String categoryId) {
		
		ProductCategory category = productCategoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Product category not found"));
		
		return productCategoryMapper.toDto(category);
	}
	
	

	@Override
	public ProductCategoryDto updateProductCategory(String productCategoryId, ProductCategoryDto categoryDto) {

		ProductCategory category = productCategoryRepository.findById(productCategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Product category not found"));
		
		category.setCategoryName(categoryDto.getCategoryName());
		category.setDescription(categoryDto.getDescription());
		
		return productCategoryMapper.toDto(productCategoryRepository.save(category));
				
	
	}



	@Override
	public void deleteProductCategory(String categoryId) {
		if(!productCategoryRepository.existsById(categoryId)) {
			throw new ResourceNotFoundException("Product category not found");
		}
		productCategoryRepository.deleteById(categoryId);
	}

	

	

}
