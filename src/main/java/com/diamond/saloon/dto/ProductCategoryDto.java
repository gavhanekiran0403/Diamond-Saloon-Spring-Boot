package com.diamond.saloon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductCategoryDto {
	
	private String productCategoryId;

	@NotBlank(message = "Product name is required")
	private String categoryName;
	
	private String description;
	
}
