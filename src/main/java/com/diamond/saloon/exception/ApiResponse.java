package com.diamond.saloon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiResponse {

    private String message;   // Response message
    private boolean success;  // true if operation succeeded, false otherwise
}
