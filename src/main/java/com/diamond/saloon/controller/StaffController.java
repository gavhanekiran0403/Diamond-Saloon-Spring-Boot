package com.diamond.saloon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.diamond.saloon.dto.StaffDto;
import com.diamond.saloon.exception.ApiResponse;
import com.diamond.saloon.service.StaffService;

@RestController
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @PostMapping("/add")
    public ResponseEntity<StaffDto> createStaff(@RequestBody StaffDto dto) {
        return new ResponseEntity<>(staffService.createStaff(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{staffId}")
    public ResponseEntity<StaffDto> updateStaff(
            @PathVariable String staffId,
            @RequestBody StaffDto dto) {
        return ResponseEntity.ok(staffService.updateStaff(staffId, dto));
    }

    @GetMapping
    public ResponseEntity<List<StaffDto>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<StaffDto> getStaffById(@PathVariable String staffId) {
        return ResponseEntity.ok(staffService.getStaffById(staffId));
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<ApiResponse> deleteStaff(@PathVariable String staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.ok(new ApiResponse("Staff deleted successfully", true));
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<StaffDto>> getBySpecialization(
            @PathVariable String specialization) {
        return ResponseEntity.ok(
                staffService.getStaffBySpecialization(specialization));
    }

    @GetMapping("/available")
    public ResponseEntity<List<StaffDto>> getAvailableStaff() {
        return ResponseEntity.ok(staffService.getAvailableStaff());
    }
}
