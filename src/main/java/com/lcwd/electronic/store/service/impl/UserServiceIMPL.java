package com.lcwd.electronic.store.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.constants.UserConstants;
import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.dto.UserDto;
import com.lcwd.electronic.store.entites.Role;
import com.lcwd.electronic.store.entites.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepositories;
import com.lcwd.electronic.store.service.UserService;

@Service
public class UserServiceIMPL implements UserService {
	@Autowired
	private UserRepositories userRepositories;
	@Autowired
	private ModelMapper mapper;
	
	@Value("${user.profile.image.path}")
	private String imageUplaodPath;
	
	@Value("${normal.role.id}")
    private String role_normal_id;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDto createUser(UserDto userDto) {
		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);
		String encodepassword = passwordEncoder.encode(userDto.getPassword());
		userDto.setPassword(encodepassword);
		User user=dtotoEntity(userDto);
		Role role = roleRepository.findById(role_normal_id).get();
		user.getRoles().add(role);
		User savedUser = userRepositories.save(user);
		UserDto newUserDto=entityToDTO(savedUser);
		return newUserDto;
	}
	
	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		User user = userRepositories.findById(userId).orElseThrow(() -> new ResourceNotFoundException(UserConstants.USER_NOT_FOUND_ID));
		user.setName(userDto.getName());
		user.setAbout(userDto.getAbout());
		user.setGender(userDto.getGender());
		user.setPassword(userDto.getPassword());
		user.setImageName(userDto.getImageName());
		User updatedUser = userRepositories.save(user);
		UserDto updatedUserDto = entityToDTO(updatedUser);
		return updatedUserDto;
	}
	@Override
	public void deleteUser(String userId) throws IOException {
		User user = userRepositories.findById(userId).orElseThrow(() -> new ResourceNotFoundException(UserConstants.USER_NOT_FOUND_ID));
		// delete user image
		String fullpath=imageUplaodPath+user.getImageName();
		Path path=Paths.get(fullpath);
		Files.delete(path);
		userRepositories.deleteById(userId);

	}
	@Override
	public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<User> page = userRepositories.findAll(pageable);
		PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
		return response;
	}
	@Override
	public UserDto getUserById(String userId) {
		User user = userRepositories.findById(userId).orElseThrow(() -> new ResourceNotFoundException(UserConstants.USER_NOT_FOUND_ID));
		return entityToDTO(user);
	}
	@Override
	public UserDto getUserByEmail(String email) {
		User user = userRepositories.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(UserConstants.USER_NOT_FOUND_EMAIL));
		return entityToDTO(user);
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		List<User> users = userRepositories.findByNameContaining(keyword);
		List<UserDto> userDtoList = users.stream().map(user -> entityToDTO(user) ).collect(Collectors.toList());
		return userDtoList;
	}
	
	 @Override
	    public Optional<User> findUserByEmailOptional(String email) {
	        return userRepositories.findByEmail(email);
	    }
	
	private UserDto entityToDTO(User savedUser) {
		/*
		 * UserDto userDto = UserDto.builder() .userId(savedUser.getUserId())
		 * .name(savedUser.getName()) .password(savedUser.getPassword())
		 * .email(savedUser.getEmail()) .gender(savedUser.getGender())
		 * .about(savedUser.getAbout()) .imageName(savedUser.getImageName()).build();
		 */
		return mapper.map(savedUser,UserDto.class);
	}

	private User dtotoEntity(UserDto userDto) {
		/*
		 * User user = User.builder() .userId(userDto.getUserId())
		 * .name(userDto.getName()) .email(userDto.getEmail())
		 * .password(userDto.getPassword()) .gender(userDto.getGender())
		 * .about(userDto.getAbout()) .imageName(userDto.getImageName()).build();
		 */
		return mapper.map(userDto, User.class);
	}

}
