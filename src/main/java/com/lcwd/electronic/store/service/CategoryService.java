package com.lcwd.electronic.store.service;

import java.io.IOException;

import com.lcwd.electronic.store.dto.CategoryDto;
import com.lcwd.electronic.store.dto.PageableResponse;

public interface CategoryService {
	//create
	 
	CategoryDto create(CategoryDto categoryDTO);
	
	//update
	CategoryDto update(CategoryDto categoryDTO,String categoryId);
	
	//delete
	void delete(String categoryId) throws IOException;
	
	//getAll
	PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
	
	//get Single
	CategoryDto getCategoryById(String categoryId);

}
