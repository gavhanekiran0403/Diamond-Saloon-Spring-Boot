package com.diamond.saloon.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.ProductCategoryDto;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.ProductCategoryMapper;
import com.diamond.saloon.model.ProductCategory;
import com.diamond.saloon.repository.ProductCategoryRepository;
import com.diamond.saloon.repository.ProductRepository;
import com.diamond.saloon.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductCategoryMapper productCategoryMapper;

	@Override
	public ProductCategoryDto addCategory(ProductCategoryDto categoryDto) {

		if (categoryDto.getCategoryName() == null || categoryDto.getCategoryName().trim().isEmpty()) {
			throw new BadRequestException("Category name is required");
		}

		String categoryName = categoryDto.getCategoryName().trim().replaceAll("\\s+", " ");

		categoryDto.setCategoryName(categoryName);

		if (productCategoryRepository.existsByCategoryNameIgnoreCase(categoryName)) {
			throw new BadRequestException("Category already exists");
		}

		if (categoryDto.getDescription() != null) {
			String desc = categoryDto.getDescription().trim();
			categoryDto.setDescription(desc.isEmpty() ? null : desc);
		}

		ProductCategory category = productCategoryMapper.toEntity(categoryDto);

		try {
			ProductCategory saved = productCategoryRepository.save(category);
			return productCategoryMapper.toDto(saved);
		} catch (DuplicateKeyException ex) {
			throw new BadRequestException("Product category already exists");
		}
	}
	

	@Override
	public List<ProductCategoryDto> getAllCategories() {

		List<ProductCategory> categories = productCategoryRepository.findAll();

		return categories.stream()
				.map(productCategoryMapper::toDto)
				.toList();
	}

	
	@Override
	public ProductCategoryDto getProductCategoryById(String categoryId) {

		if (categoryId == null || categoryId.trim().isEmpty()) {
		    throw new BadRequestException("Category ID is required");
		}

		ProductCategory category = productCategoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Product category not found"));

		return productCategoryMapper.toDto(category);
	}
	

	@Override
	public List<ProductCategoryDto> searchCategory(String keyword) {

		if (keyword == null || keyword.trim().isEmpty()) {
			throw new BadRequestException("Search keyword is required");
		}

		keyword = keyword.trim().replaceAll("\\s+", " ");

		List<ProductCategory> categories = productCategoryRepository
				.findByCategoryNameContainingIgnoreCaseOrderByCategoryNameAsc(keyword);

		return categories.stream()
				.map(productCategoryMapper::toDto)
				.toList();
	}

	@Override
	public ProductCategoryDto updateProductCategory(String categoryId, ProductCategoryDto categoryDto) {

		if (categoryId == null || categoryId.trim().isEmpty()) {
		    throw new BadRequestException("Category ID is required");
		}

		ProductCategory category = productCategoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Product category not found"));

		if (categoryDto.getCategoryName() != null && !categoryDto.getCategoryName().trim().isEmpty()) {

			String newName = categoryDto.getCategoryName().trim().replaceAll("\\s+", " ");

			if (!category.getCategoryName().equalsIgnoreCase(newName)) {

				productCategoryRepository.findByCategoryNameIgnoreCase(newName)
						.filter(existing -> !existing.getProductCategoryId().equals(categoryId)).ifPresent(existing -> {
							throw new BadRequestException("Category name already exists");
						});

				category.setCategoryName(newName);
			}
		}

		if (categoryDto.getDescription() != null) {
			String desc = categoryDto.getDescription().trim();

			if (!desc.isEmpty()) {
				category.setDescription(desc);
			}
		}

		try {
			ProductCategory updated = productCategoryRepository.save(category);
			return productCategoryMapper.toDto(updated);
		} catch (DuplicateKeyException ex) {
			throw new BadRequestException("Product category already exists");
		}
	}
	
	@Override
	public void deleteProductCategory(String categoryId) {

		if (categoryId == null || categoryId.trim().isEmpty()) {
		    throw new BadRequestException("Category ID is required");
		}

		if (!productCategoryRepository.existsById(categoryId)) {
			throw new ResourceNotFoundException("Product category not found");
		}

		boolean hasProducts = productRepository.existsByProductCategoryId(categoryId);

		if (hasProducts) {
			throw new BadRequestException("Cannot delete category with existing products");
		}

		productCategoryRepository.deleteById(categoryId);
	}

}
