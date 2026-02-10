package com.diamond.saloon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.diamond.saloon.dto.AppointmentDto;
import com.diamond.saloon.responsedto.AppointmentResponseDto;
import com.diamond.saloon.service.AppointmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/add")
    public AppointmentResponseDto createAppointment(
            @Valid @RequestBody AppointmentDto appointmentDto) {

        return appointmentService.createAppointment(appointmentDto);
    }

    @GetMapping("/get-all")
    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{appointmentId}")
    public AppointmentResponseDto getAppointmentById(
            @PathVariable String appointmentId) {

        return appointmentService.getAppointmentById(appointmentId);
    }

    @GetMapping("/user/{userId}")
    public List<AppointmentResponseDto> getAppointmentsByUserId(
            @PathVariable String userId) {

        return appointmentService.getAppointmentsByUserId(userId);
    }

    @PutMapping("/update/{appointmentId}")
    public AppointmentResponseDto updateAppointment(
            @PathVariable String appointmentId,
            @RequestBody AppointmentDto appointmentDto) {

        return appointmentService.updateAppointment(appointmentId, appointmentDto);
    }

    @PutMapping("/cancel/{appointmentId}")
    public String cancelAppointment(@PathVariable String appointmentId) {

        appointmentService.cancelAppointment(appointmentId);
        return "Appointment cancelled successfully";
    }
}
