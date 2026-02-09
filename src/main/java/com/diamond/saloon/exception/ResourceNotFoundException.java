package com.diamond.saloon.exception;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ResourceNotFoundException extends RuntimeException{

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
