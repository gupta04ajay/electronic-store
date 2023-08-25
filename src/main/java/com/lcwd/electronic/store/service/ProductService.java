package com.lcwd.electronic.store.service;

import java.io.IOException;

import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.dto.ProductDto;

public interface ProductService {
	
	
	//create
	ProductDto create(ProductDto productDto);
	//update
	ProductDto update(ProductDto productDto,String productId);
	//delete
	void delete(String productId) throws IOException;
	//getsingle
	ProductDto get(String productId);
	//getall
	PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
	//get all: live
	PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);
	//search product
	 PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);
	 
	 // create product with category
	 
	 ProductDto createProductWithCategory(ProductDto productDto, String categoryId);
	
    // Update category of product
	 ProductDto updateCategoryOfProduct(String productId, String categoryId);
	 
	 //get all of category
	 
	 PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber, int pageSize, String sortBy,
				String sortDir);
}
