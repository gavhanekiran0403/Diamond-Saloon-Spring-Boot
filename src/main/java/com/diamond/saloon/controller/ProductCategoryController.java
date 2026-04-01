package com.diamond.saloon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.common.ApiResponse;
import com.diamond.saloon.common.ApiResponseUtil;
import com.diamond.saloon.dto.ProductCategoryDto;
import com.diamond.saloon.service.ProductCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/product-categories")
public class ProductCategoryController {

	@Autowired
	private ProductCategoryService productCategoryService;

	// Create a new product category
	@PostMapping
	public ResponseEntity<ApiResponse<ProductCategoryDto>> createCategory(
			@Valid @RequestBody ProductCategoryDto categoryDto, HttpServletRequest request) {

		ProductCategoryDto saved = productCategoryService.addCategory(categoryDto);

		return ApiResponseUtil.success(
				saved, 
				"Product category created successfully", 
				HttpStatus.CREATED, 
				request
		);
	}

	
	// Get all product categories
	@GetMapping
	public ResponseEntity<ApiResponse<List<ProductCategoryDto>>> getAllCategories(HttpServletRequest request) {

		List<ProductCategoryDto> categories = productCategoryService.getAllCategories();

		return ApiResponseUtil.success(
				categories,
				"Product categories fetched successfully",
				HttpStatus.OK,
				request
		);
	}

	
	// Get product category by Id
	@GetMapping("/{productCategoryId}")
	public ResponseEntity<ApiResponse<ProductCategoryDto>> getProductCategoryById(
			@PathVariable String productCategoryId, HttpServletRequest request) {

		ProductCategoryDto category = productCategoryService.getProductCategoryById(productCategoryId);

		return ApiResponseUtil.success(
				category,
				"Product category fetched successfully",
				HttpStatus.OK,
				request
		);
	}

	
	// Search api
	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<ProductCategoryDto>>> searchCategory(
			@RequestParam String keyword, HttpServletRequest request) {

		List<ProductCategoryDto> result = productCategoryService.searchCategory(keyword);

		return ApiResponseUtil.success(
				result,
				"Category search successful",
				HttpStatus.OK,
				request
		);
	}

	
	// Update an existing product category
	@PutMapping("/{productCategoryId}")
	public ResponseEntity<ApiResponse<ProductCategoryDto>> updateCategory(@PathVariable String productCategoryId,
			@Valid @RequestBody ProductCategoryDto categoryDto, HttpServletRequest request) {

		ProductCategoryDto updated = productCategoryService.updateProductCategory(productCategoryId, categoryDto);

		return ApiResponseUtil.success(
				updated,
				"Product category updated successfully",
				HttpStatus.OK,
				request
		);
	}
	

	// Delete product category by Id
	@DeleteMapping("/{productCategoryId}")
	public ResponseEntity<ApiResponse<Void>> deleteProductCategory(
			@PathVariable String productCategoryId, HttpServletRequest request) {

		productCategoryService.deleteProductCategory(productCategoryId);

		return ApiResponseUtil.successMessage(
				"Product category deleted successfully",
				HttpStatus.OK,
				request
		);
	}

}
