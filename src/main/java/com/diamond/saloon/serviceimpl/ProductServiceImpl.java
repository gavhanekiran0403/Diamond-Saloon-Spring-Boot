package com.diamond.saloon.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.diamond.saloon.dto.ProductDto;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.ProductMapper;
import com.diamond.saloon.model.Product;
import com.diamond.saloon.model.ProductCategory;
import com.diamond.saloon.repository.ProductCategoryRepository;
import com.diamond.saloon.repository.ProductRepository;
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

	@Autowired
	private MongoTemplate mongoTemplate;
	
	private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; 

	// Add Product
	@Override
	public ProductDto addProduct(ProductDto dto, MultipartFile image) {

		validateImage(image);

		String name = normalize(dto.getProductName());
		String brand = normalize(dto.getBrand());
		String description = normalize(dto.getDescription());

		ProductCategory category = productCategoryRepository.findById(dto.getProductCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Product category not found"));

		if (productRepository.existsByProductNameIgnoreCaseAndBrandIgnoreCaseAndProductCategoryId(
				name, brand, dto.getProductCategoryId())) {
			throw new BadRequestException("Product already exists");
		}

		Product product = productMapper.toEntity(dto);

		product.setProductName(name);
		product.setBrand(brand);
		product.setDescription(description);
		product.setCategoryName(category.getCategoryName());
		product.setAvailable(dto.getStockQuantity()>0);
		
		if (dto.getAttributes() != null) {
		    product.setAttributes(normalizeAttributes(dto.getAttributes()));
		}

		product.setImageUrl(ImageUtil.ConvertToBase64(image));
		product.setCreatedAt(LocalDateTime.now());
		product.setUpdatedAt(LocalDateTime.now());

		return productMapper.toDto(productRepository.save(product));
	}

	// Get all Products
	@Override
	public List<ProductDto> getAllProducts() {

		List<Product> products = productRepository.findAll();

		return products.stream()
				.map(productMapper::toDto)
				.toList();
	}

	// Get product details
	@Override
	public ProductDto getProductById(String productId) {

		if (productId == null || productId.isBlank()) {
			throw new BadRequestException("Product ID is required");
		}

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		return productMapper.toDto(product);
	}

	// Get products by category
	@Override
	public List<ProductDto> getProductsByCategory(String categoryId) {

		if (categoryId == null || categoryId.isBlank()) {
			throw new BadRequestException("Category ID is required");
		}

		productCategoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));

		return productRepository.findByProductCategoryId(categoryId)
				.stream()
				.map(productMapper::toDto)
				.toList();
	}

	// update product details
	@Override
	public ProductDto updateProduct(String productId, ProductDto dto, MultipartFile image) {

		if (isBlank(productId)) {
			throw new BadRequestException("Product ID is required");
		}

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		String name = normalize(dto.getProductName());
		String brand = normalize(dto.getBrand());
		String description = normalize(dto.getDescription());

		ProductCategory category = productCategoryRepository.findById(dto.getProductCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category not found"));

		if (productRepository.existsByProductNameIgnoreCaseAndBrandIgnoreCaseAndProductCategoryIdAndProductIdNot(
				name, brand, dto.getProductCategoryId(), productId)) {
			throw new BadRequestException("Product already exists");
		}

		product.setProductName(name);
		product.setBrand(brand);
		product.setDescription(description);
		product.setPrice(dto.getPrice());
		product.setStockQuantity(dto.getStockQuantity());
		product.setAvailable(dto.getStockQuantity()>0);

		product.setProductCategoryId(dto.getProductCategoryId());
		product.setCategoryName(category.getCategoryName());

		if (dto.getAttributes() != null) {
		    product.setAttributes(normalizeAttributes(dto.getAttributes()));
		}

		if (image != null && !image.isEmpty()) {
			validateImage(image);
			product.setImageUrl(ImageUtil.ConvertToBase64(image));
		}
		
		product.setUpdatedAt(LocalDateTime.now());

		return productMapper.toDto(productRepository.save(product));
	}

	// Delete product
	@Override
	public void deleteProduct(String productId) {
		
		if (isBlank(productId)) {
			throw new BadRequestException("Product ID is required");
		}
		
		if (!productRepository.existsById(productId)) {
			throw new ResourceNotFoundException("Product not found");
		}

		productRepository.deleteById(productId);
	}

	// Search product
	@Override
	public List<ProductDto> searchProducts(String keyword, String brand, Double minPrice, Double maxPrice,
			String sort, String order, Boolean available) {

		List<Criteria> criteriaList = new ArrayList<>();

		if (available != null) {
		    criteriaList.add(Criteria.where("available").is(available));
		}

		
		if (!isBlank(keyword)) {
			
			String normalizedKeyword = normalize(keyword);
			criteriaList.add(Criteria.where("productName").regex(
					Pattern.compile(Pattern.quote(normalizedKeyword), Pattern.CASE_INSENSITIVE)));
		}

		if (!isBlank(brand)) {
			criteriaList.add(Criteria.where("brand")
					.regex("^" + Pattern.quote(brand.trim()) + "$", "i"));
		}

		if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
			throw new BadRequestException("Invalid price range");
		}

		if (minPrice != null) {
			criteriaList.add(Criteria.where("price").gte(minPrice));
		}

		if (maxPrice != null) {
			criteriaList.add(Criteria.where("price").lte(maxPrice));
		}

		Query query = criteriaList.isEmpty()
				? new Query()
				: new Query(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));

		if (!isBlank(sort)) {

			List<String> allowedFields = List.of("price", "productName", "brand");

			if (!allowedFields.contains(sort)) {
				throw new BadRequestException("Invalid sort field");
			}

			Sort.Direction direction = "desc".equalsIgnoreCase(order)
					? Sort.Direction.DESC
					: Sort.Direction.ASC;

			query.with(Sort.by(direction, sort));
		}

		return mongoTemplate.find(query, Product.class)
				.stream()
				.map(productMapper::toDto)
				.toList();
	}


	
	// Image validation
	private void validateImage(MultipartFile file) {

		if (file == null || file.isEmpty()) {
			throw new BadRequestException("Image is required");
		}

		if (file.getSize() > MAX_FILE_SIZE) {
			throw new BadRequestException("File size must be less than 2MB");
		}

		String contentType = file.getContentType();

		if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/jpg")
				|| contentType.equals("image/png"))) {
			throw new BadRequestException("Only JPG and PNG images are allowed");
		}
	}

	
	// Normalization method
	private String normalize(String input) {
		if (input == null || input.trim().isEmpty()) {
			throw new BadRequestException("Invalid input");
		}
		return input.trim().replaceAll("\\s+", " ");
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();

	}
	
	
	// normalize attribute
	private Map<String, String> normalizeAttributes(Map<String, String> attributes) {

	    Map<String, String> normalizedMap = new HashMap<>();

	    attributes.forEach((key, value) -> {

	        if (isBlank(key) || isBlank(value)) {
	            throw new BadRequestException("Invalid attribute key/value");
	        }

	        String normalizedKey = key.trim().toLowerCase().replaceAll("\\s+", " ");
	        String normalizedValue = value.trim().replaceAll("\\s+", " ");

	        if (normalizedMap.containsKey(normalizedKey)) {
	            throw new BadRequestException("Duplicate attribute key: " + normalizedKey);
	        }

	        normalizedMap.put(normalizedKey, normalizedValue);
	    });

	    return normalizedMap;
	}

}
	