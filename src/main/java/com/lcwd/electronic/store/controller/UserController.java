package com.lcwd.electronic.store.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
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

import com.lcwd.electronic.store.constants.UserConstants;
import com.lcwd.electronic.store.dto.ApiResponceMessage;
import com.lcwd.electronic.store.dto.ImageResponse;
import com.lcwd.electronic.store.dto.PageableResponse;
import com.lcwd.electronic.store.dto.UserDto;
import com.lcwd.electronic.store.service.FileService;
import com.lcwd.electronic.store.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
   
	private Logger logger= LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Value("${user.profile.image.path}")
	private String imageUplaodPath;

	// create
	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		UserDto user = userService.createUser(userDto);
		return new ResponseEntity<UserDto>(user, HttpStatus.CREATED);
	}

	// update
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @PathVariable("userId") String userId,
			@RequestBody UserDto userdto) {
		UserDto updateUser = userService.updateUser(userdto, userId);
		return new ResponseEntity<>(updateUser, HttpStatus.OK);
	}

	// delete
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponceMessage> deletUser(@PathVariable("userId") String userId) throws IOException {
		userService.deleteUser(userId);
		ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder().message(UserConstants.DELETE_MESSAGE)
				.success(true).status(HttpStatus.OK).build();
		return new ResponseEntity<>(apiResponceMessage, HttpStatus.OK);
	}

	// get all
	 @PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<PageableResponse<UserDto>> getAllUser(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {
		return new ResponseEntity<>(userService.getAllUser(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
	}

	// get single
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable("userId") String userId) {
		return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
	}

	// get by email
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
		return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
	}

	// search user
	 @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/search/{keywords}")
	public ResponseEntity<List<UserDto>> searchUser(@PathVariable("keywords") String keywords) {
		return new ResponseEntity<>(userService.searchUser(keywords), HttpStatus.OK);
	}

	// uplaod User Image
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image,
			@PathVariable("userId") String userId) throws IOException {
		String imageName = fileService.uploadFile(image, imageUplaodPath);
		UserDto user = userService.getUserById(userId);
		user.setImageName(imageName);
		userService.updateUser(user, userId);
		ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).success(true)
				.status(HttpStatus.CREATED).build();
		return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);
	}
	
	//serve user Image
	@GetMapping("/image/{userId}")
	public void serveUserImage(@PathVariable("userId") String userId, HttpServletResponse response) throws IOException {
		UserDto user = userService.getUserById(userId);
		logger.info("user Image Name: {} "+user.getImageName());
		InputStream resource = fileService.getResource(imageUplaodPath, user.getImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}

}
