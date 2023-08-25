package com.lcwd.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.entites.Category;

public interface CategoryRepo extends JpaRepository<Category, String> {
	

}
