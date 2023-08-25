package com.lcwd.electronic.store.services;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.lcwd.electronic.store.dto.UserDto;
import com.lcwd.electronic.store.entites.Role;
import com.lcwd.electronic.store.entites.User;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepositories;
import com.lcwd.electronic.store.service.UserService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {
	
	@MockBean
	private RoleRepository roleRepository;
	
	@MockBean
	private UserRepositories userRepositories;
	

	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper mapper;
	
	User user;
	Role role;
	String roleId;
	String userId;
	@BeforeEach
	public void init() {
		role=role.builder().roleId("abc").roleName("NORMAL").build();
		user = user.builder()
		    .name("Ajay")
		    .about("Testing mockito")
		    .email("test@mail.com")
		    .gender("male")
		    .password("abcde")
		    //.imageName("test.png")
		    .roles(Set.of(role))
		    .build();
		roleId="abc";
		userId="123456";
	}
	
	@Test
	public void createUserTest() {
		
		//jab mere userrepo se save method call ho rha hai usme koi v object pass kare inside save method mockito.any then return
		Mockito.when(userRepositories.save(Mockito.any())).thenReturn(user);
		Mockito.when(roleRepository.findById(Mockito.anyString())).thenReturn(Optional.of(role));
		
		UserDto userDto = userService.createUser(mapper.map(user, UserDto.class));
		System.out.println(userDto.getName());
		Assertions.assertNotNull(userDto);
		
	}
	
	@Test
	public void updateUserTest() {
		Mockito.when(userRepositories.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		Mockito.when(userRepositories.save(Mockito.any())).thenReturn(user);
		
		UserDto updateUser = userService.updateUser((mapper.map(user, UserDto.class)), roleId);
		System.out.println(updateUser.getEmail());
		Assertions.assertNotNull(updateUser);
		
	}
	
	

}
