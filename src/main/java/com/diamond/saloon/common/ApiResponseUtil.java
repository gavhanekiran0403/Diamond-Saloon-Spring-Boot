package com.diamond.saloon.common;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

public class ApiResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> success(
            T data,
            String message,
            HttpStatus status,
            HttpServletRequest request) {

        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(true)
                .status(status.value())
                .message(message)
                .data(data)
                .errors(null)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<ApiResponse<Void>> successMessage(
            String message,
            HttpStatus status,
            HttpServletRequest request) {

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .status(status.value())
                .message(message)
                .data(null)
                .errors(null)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(response, status);
    }
}