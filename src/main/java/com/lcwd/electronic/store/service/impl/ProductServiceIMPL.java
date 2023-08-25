package com.lcwd.electronic.store.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.dto.ProductDto;
import com.lcwd.electronic.store.entites.Category;
import com.lcwd.electronic.store.entites.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepo;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.service.ProductService;

@Service
public class ProductServiceIMPL implements ProductService {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper mapper;
	@Value("${product.image.path}")
	private String imagePath;

	@Override
	public ProductDto create(ProductDto productDto) {
		String productId = UUID.randomUUID().toString();
		Date date = new Date();
		System.out.println("Date::::::::::;" + date);
		productDto.setAddedDate(date);
		productDto.setProductId(productId);
		Product product = mapper.map(productDto, Product.class);
		Product savedProduct = productRepository.save(product);
		return mapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto update(ProductDto productDto, String productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
		product.setDescription(productDto.getDescription());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setLive(productDto.isLive());
		product.setPrice(productDto.getPrice());
		product.setQuantity(productDto.getQuantity());
		product.setStock(productDto.isStock());
		product.setTitle(productDto.getTitle());
		product.setProductImageName(productDto.getProductImageName());
		Product savedProduct = productRepository.save(product);
		return mapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public void delete(String productId) throws IOException {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
		String fullpath = imagePath + product.getProductImageName();
		Path path = Paths.get(fullpath);
		Files.delete(path);
		productRepository.deleteById(productId);
		productRepository.delete(product);
	}

	@Override
	public ProductDto get(String productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
		return mapper.map(product, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findAll(pageable);
		PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
		return pageableResponse;
	}

	@Override
	public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByLiveTrue(pageable);
		PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
		return pageableResponse;

	}

	@Override
	public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy,
			String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByTitleContaining(subTitle, pageable);
		PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
		return pageableResponse;
	}

	@Override
	public ProductDto createProductWithCategory(ProductDto productDto, String categoryId) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Id not found"));
		String productId = UUID.randomUUID().toString();
		Date date = new Date();
		System.out.println("Date::::::::::;" + date);
		productDto.setAddedDate(date);
		productDto.setProductId(productId);
		Product product = mapper.map(productDto, Product.class);
		product.setCategory(category);
		Product savedProduct = productRepository.save(product);
		return mapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateCategoryOfProduct(String productId, String categoryId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
		product.setCategory(category);
		Product savedProductWithCategoryId = productRepository.save(product);
		return mapper.map(savedProductWithCategoryId, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy,
			String sortDir) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = productRepository.findByCategory(category, pageable);
		PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
		return pageableResponse;
	}

}
