package com.diamond.saloon.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String resourceName; // e.g., "Service"
    private String fieldName;    // e.g., "serviceId"
    private String fieldValue;   // e.g., "12345"

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
