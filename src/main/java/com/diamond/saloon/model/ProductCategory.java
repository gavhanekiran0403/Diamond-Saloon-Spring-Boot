package com.diamond.saloon.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



import lombok.Data;

@Document(collection = "product_categories")
@Data
public class ProductCategory {

	@Id
	private String productCategoryId;
	
	private String categoryName;
	
	private String description;
}
