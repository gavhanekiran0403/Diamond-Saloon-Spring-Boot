package com.diamond.saloon.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.diamond.saloon.common.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Resource not found
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(
			ResourceNotFoundException ex, HttpServletRequest request) {

		ApiResponse<Object> response = ApiResponse.builder()
				.success(false)
				.status(HttpStatus.NOT_FOUND.value())
				.message(ex.getMessage())
				.data(null)
				.errors(null)
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	

	// Bad request
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiResponse<Object>> handleBadRequest(
			BadRequestException ex, HttpServletRequest request) {

		ApiResponse<Object> response = ApiResponse.builder()
				.success(false)
				.status(HttpStatus.BAD_REQUEST.value())
				.message(ex.getMessage())
				.data(null)
				.errors(null)
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	
	// Validation errors
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Object>> handleValidationException(
			MethodArgumentNotValidException ex, HttpServletRequest request) {

		Map<String, String> validationErrors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> validationErrors.put(error.getField(), error.getDefaultMessage()));

		ApiResponse<Object> response = ApiResponse.builder()
				.success(false)
				.status(HttpStatus.BAD_REQUEST.value())
				.message("Validation failed")
				.data(null)
				.errors(validationErrors)
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	

	// Invalid JSON request body
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse<Object>> handleInvalidJson(
			HttpMessageNotReadableException ex, HttpServletRequest request) {

		ApiResponse<Object> response = ApiResponse.builder()
				.success(false)
				.status(HttpStatus.BAD_REQUEST.value())
				.message("Invalid request body")
				.data(null)
				.errors(null)
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	
	// MongoDB duplicate key
	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<ApiResponse<Object>> handleDuplicateKey(
			DuplicateKeyException ex, HttpServletRequest request) {

		ApiResponse<Object> response = ApiResponse.builder()
				.success(false)
				.status(HttpStatus.CONFLICT.value())
				.message("Duplicate value found")
				.data(null)
				.errors(null)
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	
	// Global exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleGlobalException(
			Exception ex, HttpServletRequest request) {

		ApiResponse<Object> response = ApiResponse.builder()
				.success(false)
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message("Something went wrong. Please try again later.")
				.data(null)
				.errors(null)
				.path(request.getRequestURI())
				.timestamp(LocalDateTime.now())
				.build();

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}