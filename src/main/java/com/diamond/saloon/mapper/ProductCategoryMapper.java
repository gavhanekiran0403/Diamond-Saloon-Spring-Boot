package com.diamond.saloon.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.diamond.saloon.dto.ProductCategoryDto;
import com.diamond.saloon.model.ProductCategory;

public class ProductCategoryMapper {
	
	@Autowired
	private ModelMapper modelMapper;

	public  ProductCategory toEntity (ProductCategoryDto productCategoryDto) {
		
		
		return modelMapper.map(productCategoryDto, ProductCategory.class);
		
	}
	
	public  void updateEntity(ProductCategoryDto dto, ProductCategory entity) {
		modelMapper.map(dto, entity);
	}
	
}
