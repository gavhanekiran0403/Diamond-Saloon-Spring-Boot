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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.diamond.saloon.common.ApiResponse;
import com.diamond.saloon.common.ApiResponseUtil;
import com.diamond.saloon.dto.ProductDto;
import com.diamond.saloon.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	// Add new product
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<ProductDto>> addProduct(
			@Valid @RequestPart("product") ProductDto productDto,
			@RequestPart("image") MultipartFile image, HttpServletRequest request) {

		ProductDto saved = productService.addProduct(productDto, image);

		return ApiResponseUtil.success(
				saved,
				"Product created successfully",
				HttpStatus.CREATED,
				request
		);
	}

	// get all products
	@GetMapping
	public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts(HttpServletRequest request) {

		List<ProductDto> products = productService.getAllProducts();

		return ApiResponseUtil.success(
				products,
				"Products fetched successfully",
				HttpStatus.OK,
				request
		);
	}

	// Get product by id
	@GetMapping("/{productId}")
	public ResponseEntity<ApiResponse<ProductDto>> getProductById(
			@PathVariable String productId, HttpServletRequest request) {

		ProductDto product = productService.getProductById(productId);

		return ApiResponseUtil.success(
				product,
				"Product fetched successfully",
				HttpStatus.OK,
				request
		);
	}

	// get products by category
	@GetMapping("/category/{productCategoryId}")
	public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByCategory(
			@PathVariable String productCategoryId, HttpServletRequest request) {

		List<ProductDto> products = productService.getProductsByCategory(productCategoryId);

		return ApiResponseUtil.success(
				products,
				"Products fetched successfully by category",
				HttpStatus.OK,
				request
		);
	}

	// update product by id
	@PutMapping(value = "/{productId}", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
			@PathVariable String productId, @Valid @RequestPart("product") ProductDto productDto,
			@RequestPart(value = "image", required = false) MultipartFile image, HttpServletRequest request) {

		ProductDto updated = productService.updateProduct(productId, productDto, image);

		return ApiResponseUtil.success(
				updated,
				"Product updated successfully",
				HttpStatus.OK,
				request
		);
	}

	// delete product by id
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponse<Void>> deleteProduct(
			@PathVariable String productId, HttpServletRequest request) {

		productService.deleteProduct(productId);

		return ApiResponseUtil.successMessage(
				"Product deleted successfully",
				HttpStatus.OK,
				request
		);
	}

	// Search products by keyword, brand, and price range
	@GetMapping("/search")
	public ResponseEntity<ApiResponse<List<ProductDto>>> searchProducts(
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String brand, 
			@RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice, 
			@RequestParam(required = false) String sort,
			@RequestParam(required = false) String order, 
			@RequestParam(required = false) Boolean available, HttpServletRequest request) {

		List<ProductDto> products = productService.searchProducts(keyword, brand, minPrice, maxPrice, sort, order, available);

		return ApiResponseUtil.success(
				products,
				"Products search successful",
				HttpStatus.OK,
				request
		);
	}

}
