package com.diamond.saloon.mapper;

import com.diamond.saloon.dto.ProductCategoryDto;
import com.diamond.saloon.model.ProductCategory;

public class ProductCategoryMapper {

	public static ProductCategory toEntity (ProductCategoryDto productCategory) {
		
		ProductCategory category = new ProductCategory();
		
		category.setCategoryName(productCategory.getCategoryName());
		category.setDescription(productCategory.getDescription());
		
		return category;
		
	}
	
	public static void updateEntity(ProductCategory category, ProductCategoryDto dto) {
		category.setCategoryName(dto.getCategoryName());
		category.setDescription(dto.getDescription());
	}
	
}
