package com.diamond.saloon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.diamond.saloon.dto.ServiceDto;
import com.diamond.saloon.exception.ApiResponse;
import com.diamond.saloon.service.ServiceService;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    // Create a new service
    @PostMapping
    public ResponseEntity<ServiceDto> createService(@RequestBody ServiceDto serviceDto) {
        ServiceDto savedService = serviceService.createService(serviceDto);
        return new ResponseEntity<>(savedService, HttpStatus.CREATED);
    }

    // Update an existing service
    @PutMapping("/{serviceId}")
    public ResponseEntity<ServiceDto> updateService(
            @PathVariable String serviceId,
            @RequestBody ServiceDto serviceDto) {
        ServiceDto updatedService = serviceService.updateService(serviceId, serviceDto);
        return new ResponseEntity<>(updatedService, HttpStatus.OK);
    }

    // Get all services
    @GetMapping
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        List<ServiceDto> services = serviceService.getAllServices();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    // Get service by ID
    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable String serviceId) {
        ServiceDto service = serviceService.getServiceById(serviceId);
        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    // Delete a service
    @DeleteMapping("/{serviceId}")
    public ResponseEntity<ApiResponse> deleteService(@PathVariable String serviceId) {
        serviceService.deleteService(serviceId);
        return new ResponseEntity<>(new ApiResponse("Service deleted successfully", true), HttpStatus.OK);
    }

    // Get services by category (MEN/WOMEN)
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ServiceDto>> getServicesByCategory(@PathVariable String category) {
        List<ServiceDto> services = serviceService.getServicesByCategory(category);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    // Get all active services
    @GetMapping("/active")
    public ResponseEntity<List<ServiceDto>> getActiveServices() {
        List<ServiceDto> activeServices = serviceService.getActiveServices();
        return new ResponseEntity<>(activeServices, HttpStatus.OK);
    }
}
