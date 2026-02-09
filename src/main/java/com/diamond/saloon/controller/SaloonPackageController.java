package com.diamond.saloon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.diamond.saloon.dto.SaloonPackageDto;
import com.diamond.saloon.exception.ApiResponse;
import com.diamond.saloon.service.SaloonPackageService;

@RestController
@RequestMapping("/saloon-packages")
public class SaloonPackageController {

    @Autowired
    private SaloonPackageService saloonPackageService;

    @PostMapping("/add")
    public ResponseEntity<SaloonPackageDto> createPackage(@RequestBody SaloonPackageDto dto) {
        return new ResponseEntity<>(
                saloonPackageService.createPackage(dto),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{packageId}")
    public ResponseEntity<SaloonPackageDto> updatePackage(
            @PathVariable String packageId,
            @RequestBody SaloonPackageDto dto) {
        return ResponseEntity.ok(
                saloonPackageService.updatePackage(packageId, dto)
        );
    }

    @GetMapping
    public ResponseEntity<List<SaloonPackageDto>> getAllPackages() {
        return ResponseEntity.ok(
                saloonPackageService.getAllPackages()
        );
    }

    @GetMapping("/{packageId}")
    public ResponseEntity<SaloonPackageDto> getPackageById(@PathVariable String packageId) {
        return ResponseEntity.ok(
                saloonPackageService.getPackageById(packageId)
        );
    }

    @DeleteMapping("/{packageId}")
    public ResponseEntity<ApiResponse> deletePackage(@PathVariable String packageId) {
        saloonPackageService.deletePackage(packageId);
        return ResponseEntity.ok(
                new ApiResponse("Package deleted successfully", true)
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<SaloonPackageDto>> getPackagesByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(
                saloonPackageService.getPackagesByCategory(category)
        );
    }
}
