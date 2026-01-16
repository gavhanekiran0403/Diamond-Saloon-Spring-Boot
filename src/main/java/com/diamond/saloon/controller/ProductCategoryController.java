package com.diamond.saloon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.dto.ProductCategoryDto;
import com.diamond.saloon.model.ProductCategory;
import com.diamond.saloon.service.ProductCategoryService;

@RestController
@RequestMapping("/product-categories")
public class ProductCategoryController {
	
	
	@Autowired
	private ProductCategoryService ProductCategoryService;
	
	
	
	//Create a new product category
	@PostMapping("/create")
	public ProductCategoryDto createCategory(@RequestBody ProductCategoryDto categoryDto) {
		return ProductCategoryService.addCategory(categoryDto);
		
	}
	
	
	
	//Get all product categories
	@GetMapping("/get-all")
	public List<ProductCategoryDto> getAllCategories(){
		return ProductCategoryService.getAllCategories();
		
	}
	
	
	
	//Get product category by Id
	@GetMapping("/{productCategoryId}")
	public ProductCategoryDto getProductCategoryById(@PathVariable String productCategoryId) {
		return ProductCategoryService.getProductCategoryById(productCategoryId);
	}
	
	
	
	//Update an existing product category
	@PutMapping("/update/{productCategoryId}")
	public ProductCategoryDto updateCategory(@PathVariable String productCategoryId, @RequestBody ProductCategoryDto categoryDto) {
		return ProductCategoryService.updateProductCategory(productCategoryId, categoryDto);
		
	}

	

	//Delete product category by Id
	@DeleteMapping("/delete/{productCategoryId}")
	public String deleteProductCategory(@PathVariable String productCategoryId) {
		ProductCategoryService.deleteProductCategory(productCategoryId);
		return "Product category deleted Successfully";
		
	}
	
	
}
