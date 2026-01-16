package com.diamond.saloon.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.ProductDto;
import com.diamond.saloon.model.Product;



@Component
public class ProductMapper {

	@Autowired
	private ModelMapper modelMapper;

	
	public Product toEntity(ProductDto dto) {
		return modelMapper.map(dto, Product.class);
	}
	
	
	public ProductDto toDto(Product entity) {
		return modelMapper.map(entity, ProductDto.class);
	}
}
