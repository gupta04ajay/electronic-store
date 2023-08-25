package com.lcwd.electronic.store.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.entites.User;

public interface UserRepositories  extends JpaRepository<User, String>{
	
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailAndName(String email, String name);
    List<User> findByNameContaining(String keywords);
}
