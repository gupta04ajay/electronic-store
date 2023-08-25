package com.lcwd.electronic.store.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dto.CategoryDto;
import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.entites.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepo;
import com.lcwd.electronic.store.service.CategoryService;

@Service
public class CategoryServiceIMPL implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ModelMapper mapper;
	
	@Value("${category.image.path}")
	private String imageUplaodPath;

	@Override
	public CategoryDto create(CategoryDto categoryDTO) {
		String categoryId = UUID.randomUUID().toString();
		categoryDTO.setCategoryId(categoryId);
		Category category = mapper.map(categoryDTO, Category.class);
		Category savedCategory = categoryRepo.save(category);
		return mapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto update(CategoryDto categoryDTO, String categoryId) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Id Not Found"));
		category.setTitle(categoryDTO.getTitle());
		category.setCoverImage(categoryDTO.getCoverImage());
		category.setDescription(categoryDTO.getDescription());
		Category updateCategory = categoryRepo.save(category);
		return mapper.map(updateCategory, CategoryDto.class);
	}

	@Override
	public void delete(String categoryId) throws IOException {
		Category categorie = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Id Not Found"));
		String fullpath=imageUplaodPath+categorie.getCoverImage();
		Path path=Paths.get(fullpath);
		Files.delete(path);
		categoryRepo.deleteById(categoryId);
	}

	@Override
	 public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> page = categoryRepo.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;
    }

	@Override
	public CategoryDto getCategoryById(String categoryId) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category Id Not Found"));
		return mapper.map(category, CategoryDto.class);
	}
	
	
}
