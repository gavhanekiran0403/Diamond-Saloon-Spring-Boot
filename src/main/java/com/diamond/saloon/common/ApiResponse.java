package com.diamond.saloon.common;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ApiResponse<T> {

	private boolean success;
    private int status;
    private String message;
    private T data;
    private Object errors;
    private String path;
    private LocalDateTime timestamp;
    
}
