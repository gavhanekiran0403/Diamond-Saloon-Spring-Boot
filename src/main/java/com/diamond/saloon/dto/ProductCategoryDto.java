package com.diamond.saloon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductCategoryDto {

	private String productCategoryId;

	@NotBlank(message = "Product Category name is required")
	@Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
	@Pattern(
			regexp = "^[a-zA-Z0-9 &-]+$", 
			message = "Category name contains invalid characters"
		)
	private String categoryName;

	@Size(max = 200, message = "Description cannot exceed 200 characters")
	private String description;

}
