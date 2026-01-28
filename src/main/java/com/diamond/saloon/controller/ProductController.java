package com.diamond.saloon.controller;

import java.util.List; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.diamond.saloon.dto.ProductDto;
import com.diamond.saloon.responsedto.ProductResponseDto;
import com.diamond.saloon.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	
	// Add new product
	@PostMapping(value = "/add", consumes = "multipart/form-data")
	public ProductResponseDto addProduct(@Valid @RequestPart("product") ProductDto productDto, 
						@RequestPart("image") MultipartFile image)  {
		
		return productService.addProduct(productDto, image);	
	}
	
	
	// get all products
	@GetMapping("/get-all")
	public List<ProductResponseDto> getAllProducts() {
		return productService.getAllProducts();
		
	}
	
	
	// get product by id
	@GetMapping("/{productId}")
	public ProductResponseDto getProductById(@PathVariable String productId) {
		return productService.getProductById(productId);
	}
	
	
	// get products by category
	@GetMapping("/get-by-category/{productCategoryId}")
	public List<ProductResponseDto> getProductsByCategory(@PathVariable String productCategoryId){
		return productService.getProductsByCategory(productCategoryId);
	}
	
	
	// update product by id
	@PutMapping(value = "/update/{productId}", consumes = "multipart/form-data")
	public ProductResponseDto updateProduct(@PathVariable String productId, 
			@RequestPart("product") ProductDto productDto, 
			@RequestPart("image") MultipartFile image) {
					
		return productService.updateProduct(productId, productDto, image);
		
	}
	
			
	// delete product by id
	@DeleteMapping("/delete/{productId}")
	public String deleteProduct(@PathVariable String productId) {
		productService.deleteProduct(productId);
		return "Product deleted successfully";
	}
	
}
