package com.diamond.saloon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.diamond.saloon.dto.SaloonServiceDto;
import com.diamond.saloon.exception.ApiResponse;
import com.diamond.saloon.service.SaloonService;

@RestController
@RequestMapping("/saloon-services")
public class SaloonServiceController {

    @Autowired
    private SaloonService saloonService;

    // Create a new service
    @PostMapping("/add")
    public ResponseEntity<SaloonServiceDto> createService(@RequestBody SaloonServiceDto serviceDto) {
        SaloonServiceDto savedService = saloonService.createService(serviceDto);
        return new ResponseEntity<>(savedService, HttpStatus.CREATED);
    }

    // Update an existing service
    @PutMapping("/{serviceId}")
    public ResponseEntity<SaloonServiceDto> updateService(
            @PathVariable String serviceId,
            @RequestBody SaloonServiceDto serviceDto) {
        SaloonServiceDto updatedService = saloonService.updateService(serviceId, serviceDto);
        return new ResponseEntity<>(updatedService, HttpStatus.OK);
    }

    // Get all services
    @GetMapping
    public ResponseEntity<List<SaloonServiceDto>> getAllServices() {
        List<SaloonServiceDto> services = saloonService.getAllServices();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    // Get service by ID
    @GetMapping("/{serviceId}")
    public ResponseEntity<SaloonServiceDto> getServiceById(@PathVariable String serviceId) {
        SaloonServiceDto service = saloonService.getServiceById(serviceId);
        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    // Delete a service
    @DeleteMapping("/{serviceId}")
    public ResponseEntity<ApiResponse> deleteService(@PathVariable String serviceId) {
    	saloonService.deleteService(serviceId);
        return new ResponseEntity<>(new ApiResponse("Service deleted successfully", true), HttpStatus.OK);
    }

    // Get services by category (MEN/WOMEN)
    @GetMapping("/category/{category}")
    public ResponseEntity<List<SaloonServiceDto>> getServicesByCategory(@PathVariable String category) {
        List<SaloonServiceDto> services = saloonService.getServicesByCategory(category);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    // Get all active services
    @GetMapping("/active")
    public ResponseEntity<List<SaloonServiceDto>> getActiveServices() {
        List<SaloonServiceDto> activeServices = saloonService.getActiveServices();
        return new ResponseEntity<>(activeServices, HttpStatus.OK);
    }
}
