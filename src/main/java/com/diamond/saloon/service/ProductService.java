package com.diamond.saloon.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.diamond.saloon.dto.ProductDto;

public interface ProductService {

	public ProductDto addProduct(ProductDto productDto, MultipartFile image);

	public List<ProductDto> getAllProducts();

	public ProductDto getProductById(String productId);

	public List<ProductDto> getProductsByCategory(String categoryId);

	public ProductDto updateProduct(String productId, ProductDto productDto, MultipartFile image);

	public void deleteProduct(String productId);

	public List<ProductDto> searchProducts(
			String keyword, 
			String brand, 
			Double minPrice, 
			Double maxPrice, 
			String sort,
			String order,
			Boolean available);

}
