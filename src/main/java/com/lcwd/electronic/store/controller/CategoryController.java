package com.lcwd.electronic.store.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.dto.ApiResponceMessage;
import com.lcwd.electronic.store.dto.CategoryDto;
import com.lcwd.electronic.store.dto.ImageResponse;
import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.dto.ProductDto;
import com.lcwd.electronic.store.service.CategoryService;
import com.lcwd.electronic.store.service.FileService;
import com.lcwd.electronic.store.service.ProductService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private FileService fileService;

	@Value("${category.image.path}")
	private String imageUplaodPath;

	// create
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		// call service to save object
		CategoryDto categoryDto1 = categoryService.create(categoryDto);
		return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
	}

	// update
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable String categoryId,
			@RequestBody CategoryDto categoryDto) {
		CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
		return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
	}

	// delete
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponceMessage> deleteCategory(@PathVariable String categoryId) throws IOException {
		categoryService.delete(categoryId);
		ApiResponceMessage responseMessage = ApiResponceMessage.builder().message("Category is deleted successfully !!")
				.status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<>(responseMessage, HttpStatus.OK);

	}

	// get all

	@GetMapping
	public ResponseEntity<PageableResponse<CategoryDto>> getAll(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

	) throws InterruptedException {
		// Thread.sleep(1000);
		PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}

	// get single
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId) {
		CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
		return ResponseEntity.ok(categoryDto);
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/image/{categoryId}")
	public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("categoryImage") MultipartFile categoryImage,
			@PathVariable("categoryId") String categoryId) throws IOException {
		System.out.println("ImageUpload::::::::::::");
		String imageName = fileService.uploadFile(categoryImage, imageUplaodPath);
		CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
		categoryDto.setCoverImage(imageName);
		categoryService.update(categoryDto, categoryId);
		ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true)
				.status(HttpStatus.CREATED).build();
		return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
	}
    
	// create Product with category
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{categoryId}/products")
	public ResponseEntity<ProductDto> createProducWithCategory(@PathVariable("categoryId") String categoryId,
			@RequestBody ProductDto productDto) {
		ProductDto productWithCategory = productService.createProductWithCategory(productDto, categoryId);
		return new ResponseEntity<>(productWithCategory, HttpStatus.CREATED);
	}

	// update category of product
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{categoryId}/products/{productId}")
	public ResponseEntity<ProductDto> updateCategoryOfProduct(@PathVariable("categoryId") String categoryId,
			@PathVariable("productId") String productId) {
		ProductDto updateCategoryOfProduct = productService.updateCategoryOfProduct(productId, categoryId);
		return new ResponseEntity<>(updateCategoryOfProduct, HttpStatus.OK);
	}
   
	//get product of category
	@GetMapping("/{categoryId}/products")
	public ResponseEntity<PageableResponse<ProductDto>> getAllCategoryProduct(@PathVariable String categoryId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
		PageableResponse<ProductDto> response = productService.getAllOfCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
}
