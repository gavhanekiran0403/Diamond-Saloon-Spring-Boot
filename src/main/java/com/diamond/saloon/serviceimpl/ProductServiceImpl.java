package com.diamond.saloon.serviceimpl;

import java.util.List; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.diamond.saloon.dto.ProductDto;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.ProductMapper;
import com.diamond.saloon.model.Product;
import com.diamond.saloon.model.ProductCategory;
import com.diamond.saloon.repository.ProductCategoryRepository;
import com.diamond.saloon.repository.ProductRepository;
import com.diamond.saloon.responsedto.ProductResponseDto;
import com.diamond.saloon.service.ProductService;
import com.diamond.saloon.util.ImageUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private ProductMapper productMapper;
	

	//utility method
	private ProductResponseDto buildResponse(Product product) {

		ProductCategory category = productCategoryRepository.findById(product.getProductCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Ctegory not found"));

		ProductResponseDto response = productMapper.toDto(product);
		response.setCategoryName(category.getCategoryName());
		
		return response;
	}
	
	
	

	@Override
	public ProductResponseDto addProduct(ProductDto productDto, MultipartFile image) {

		ProductCategory category = productCategoryRepository.findById(productDto.getProductCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Product category not found"));

		Product product = productMapper.toEntity(productDto);
		

		if (image != null && !image.isEmpty()) {
			product.setImageUrl(ImageUtil.ConvertToBase64(image));
		}

		return buildResponse(productRepository.save(product));

		
	}

	
	
	@Override
	public List<ProductResponseDto> getAllProducts() {

		return productRepository.findAll()
				.stream()
				.map(this::buildResponse)
				.toList();
	}

	
	
	@Override
	public ProductResponseDto getProductById(String productId) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		return buildResponse(product);
	}

	
	
	@Override
	public List<ProductResponseDto> getProductsByCategory(String categoryId) {

		productCategoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));

		return productRepository.findByProductCategoryId(categoryId)
				.stream()
				.map(this::buildResponse)
				.toList();
	}

	
	
	@Override
	public ProductResponseDto updateProduct(String productId, ProductDto productDto, MultipartFile image) {

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		

		if (!product.getProductCategoryId().equals(productDto.getProductCategoryId())) {
			productCategoryRepository.findById(productDto.getProductCategoryId())
					.orElseThrow(() -> new ResourceNotFoundException("Product category not found"));
			product.setProductCategoryId(productDto.getProductCategoryId());
		}
		

		product.setProductName(productDto.getProductName());
		product.setBrand(productDto.getBrand());
		product.setDescription(productDto.getDescription());
		product.setAttributes(productDto.getAttributes());
		product.setPrice(productDto.getPrice());
		product.setStockQuantity(productDto.getStockQuantity());
		product.setAvailable(productDto.isAvailable());

		if (image != null && !image.isEmpty()) {
			product.setImageUrl(ImageUtil.ConvertToBase64(image));
		}
		
		return buildResponse(productRepository.save(product));
	}



	@Override
	public void deleteProduct(String productId) {
		if(!productRepository.existsById(productId)) {
			throw new ResourceNotFoundException("Product not found");
		}
		
		productRepository.deleteById(productId);
	}

}
