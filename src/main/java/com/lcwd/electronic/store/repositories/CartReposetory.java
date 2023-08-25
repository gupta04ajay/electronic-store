package com.lcwd.electronic.store.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.entites.Cart;
import com.lcwd.electronic.store.entites.User;

public interface CartReposetory extends JpaRepository<Cart, String> {
     
	 Optional<Cart> findByUser(User user);
}
