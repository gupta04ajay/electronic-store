package com.lcwd.electronic.store.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lcwd.electronic.store.dto.ApiResponceMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	
	// handle resource not found exception
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponceMessage> resourceNotFoundException(ResourceNotFoundException ex) {
		logger.info("Exception Handler Invoke");
		ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder().message(ex.getMessage())
				.status(HttpStatus.NOT_FOUND).success(true).build();
		return new ResponseEntity<>(apiResponceMessage, HttpStatus.NOT_FOUND);
	}
	
	//methodArgumentNotValidException 
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
		Map<String, Object> errorMap = new HashMap<String, Object>();
		allErrors.stream().forEach(objectError -> {
			String message = objectError.getDefaultMessage();
			String field = ((FieldError) objectError).getField();
			errorMap.put(field, message);
		});
		return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BadApiRequest.class)
	public ResponseEntity<ApiResponceMessage> handleBadApiRequest(BadApiRequest ex) {
		logger.info("BAd APi Request");
		ApiResponceMessage apiResponceMessage = ApiResponceMessage.builder().message(ex.getMessage())
				.status(HttpStatus.BAD_REQUEST).success(false).build();
		return new ResponseEntity<>(apiResponceMessage, HttpStatus.BAD_REQUEST);
	}
}
