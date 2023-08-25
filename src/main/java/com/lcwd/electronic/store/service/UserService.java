package com.lcwd.electronic.store.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.dto.UserDto;
import com.lcwd.electronic.store.entites.User;

public interface UserService {
	
	//create user
	UserDto createUser(UserDto user);
	
	
	//updateuser
	UserDto updateUser(UserDto user, String userId);
	
	//delete
	 void deleteUser(String userId) throws IOException;
	 
	//getalluser
	
	 PageableResponse<UserDto> getAllUser(int pageNumber,int pageSize,String sortBy, String sortDir);
	//get single user by id
	
	 UserDto getUserById(String userId);
	//get single user by email
	 UserDto getUserByEmail(String email);
	 
	//search user
	 List<UserDto> searchUser(String keyword);
	 
	 Optional<User> findUserByEmailOptional(String email);

}
