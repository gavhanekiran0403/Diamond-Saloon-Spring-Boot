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
	
	@PostMapping("/create")
	public ProductCategory createCategory(@RequestBody ProductCategoryDto categoryDto) {
		return ProductCategoryService.addCategory(categoryDto);
		
	}
	
	
	@GetMapping("/get-all")
	public List<ProductCategory> getAllCategories(){
		return ProductCategoryService.getAllCategories();
		
	}
	
	
	@GetMapping("/{productCategoryId}")
	public ProductCategory getProductCategoryById(@PathVariable String productCategoryId) {
		return ProductCategoryService.getProductCategoryById(productCategoryId);
	}
	
	
	@PutMapping("/update/{productCategoryId}")
	public ProductCategory updateCategory(@PathVariable String productCategoryId, @RequestBody ProductCategoryDto categoryDto) {
		return ProductCategoryService.updateProductCategory(productCategoryId, categoryDto);
		
	}


	@DeleteMapping("/delete/{productCategoryId}")
	public String deleteProductCategory(@PathVariable String productCategoryId) {
		ProductCategoryService.deleteProductCategory(productCategoryId);
		return "Product category deleted Successfully";
		
	}
	
	
}
