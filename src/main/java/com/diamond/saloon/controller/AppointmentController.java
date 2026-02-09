package com.diamond.saloon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.diamond.saloon.dto.AppointmentDto;
import com.diamond.saloon.exception.ApiResponse;
import com.diamond.saloon.service.AppointmentService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/add")
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentDto dto) {
        AppointmentDto saved = appointmentService.createAppointment(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        return new ResponseEntity<>(appointmentService.getAllAppointments(), HttpStatus.OK);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable String appointmentId) {
        return new ResponseEntity<>(appointmentService.getAppointmentById(appointmentId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByUser(@PathVariable String userId) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByStatus(@PathVariable String status) {
        return new ResponseEntity<>(appointmentService.getAppointmentsByStatus(status), HttpStatus.OK);
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> updateAppointment(
            @PathVariable String appointmentId,
            @RequestBody AppointmentDto dto) {
        return new ResponseEntity<>(
                appointmentService.updateAppointment(appointmentId, dto),
                HttpStatus.OK
        );
    }

    @PutMapping("/cancel/{appointmentId}")
    public ResponseEntity<ApiResponse> cancelAppointment(@PathVariable String appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return new ResponseEntity<>(
                new ApiResponse("Appointment cancelled successfully", true),
                HttpStatus.OK
        );
    }
}
