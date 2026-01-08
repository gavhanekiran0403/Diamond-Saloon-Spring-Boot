package com.diamond.saloon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
		return  new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
				
	}
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
		return  new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		
	}
	
}
