package com.diamond.saloon.service;

import java.util.List; 

import org.springframework.web.multipart.MultipartFile;

import com.diamond.saloon.dto.ProductDto;
import com.diamond.saloon.responsedto.ProductResponseDto;

public interface ProductService {

	public ProductResponseDto addProduct(ProductDto productDto, MultipartFile image);
	
	public List<ProductResponseDto> getAllProducts();
	
	public ProductResponseDto getProductById(String productId);
	
	public List<ProductResponseDto> getProductsByCategory(String categoryId);
	
	public ProductResponseDto updateProduct(String productId, ProductDto productDto, MultipartFile image);
	
	public void deleteProduct(String productId);
}
