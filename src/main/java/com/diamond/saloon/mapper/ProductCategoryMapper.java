package com.diamond.saloon.mapper;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.ProductCategoryDto;
import com.diamond.saloon.model.ProductCategory;


@Component
public class ProductCategoryMapper {
	
	@Autowired
	private ModelMapper modelMapper;

	public  ProductCategory toEntity (ProductCategoryDto productCategoryDto) {
		
		
		return modelMapper.map(productCategoryDto, ProductCategory.class);
		
	}
	
	
	public  ProductCategoryDto toDto(ProductCategory entity) {
		return modelMapper.map(entity, ProductCategoryDto.class);
	}
	
}
