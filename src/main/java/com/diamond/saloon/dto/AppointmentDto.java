package com.diamond.saloon.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentDto {

    private String appointmentId;

    @NotBlank(message = "User id is required")
    private String userId;

    private String serviceId;
    private String packageId;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    @NotBlank(message = "Time slot is required")
    private String timeSlot;

    private String status;
}
