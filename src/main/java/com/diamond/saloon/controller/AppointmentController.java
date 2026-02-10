package com.diamond.saloon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.diamond.saloon.dto.AppointmentDto;
import com.diamond.saloon.exception.ApiResponse;
import com.diamond.saloon.responsedto.AppointmentResponseDto;
import com.diamond.saloon.service.AppointmentService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;


    // ✅ CREATE APPOINTMENT
    @PostMapping("/add")
    public ResponseEntity<AppointmentResponseDto> createAppointment(
            @RequestBody AppointmentDto dto) {

        AppointmentResponseDto response =
                appointmentService.createAppointment(dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // ✅ GET ALL APPOINTMENTS
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {

        List<AppointmentResponseDto> responses =
                appointmentService.getAllAppointments();

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    // ✅ GET BY ID
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(
            @PathVariable String appointmentId) {

        AppointmentResponseDto response =
                appointmentService.getAppointmentById(appointmentId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // ✅ GET BY USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByUser(
            @PathVariable String userId) {

        List<AppointmentResponseDto> responses =
                appointmentService.getAppointmentsByUserId(userId);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    // ✅ GET BY STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByStatus(
            @PathVariable String status) {

        List<AppointmentResponseDto> responses =
                appointmentService.getAppointmentsByStatus(status);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    // ✅ UPDATE APPOINTMENT
    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(
            @PathVariable String appointmentId,
            @RequestBody AppointmentDto dto) {

        AppointmentResponseDto response =
                appointmentService.updateAppointment(appointmentId, dto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // ✅ CANCEL APPOINTMENT
    @PutMapping("/cancel/{appointmentId}")
    public ResponseEntity<ApiResponse> cancelAppointment(
            @PathVariable String appointmentId) {

        appointmentService.cancelAppointment(appointmentId);

        return new ResponseEntity<>(
                new ApiResponse("Appointment cancelled successfully", true),
                HttpStatus.OK
        );
    }
}
