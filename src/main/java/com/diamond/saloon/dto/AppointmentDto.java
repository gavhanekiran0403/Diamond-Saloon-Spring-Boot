package com.diamond.saloon.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AppointmentDto {

    private String appointmentId;

    private String userId;

    private String serviceId;

    private String packageId;

    private LocalDate appointmentDate;

    private String timeSlot;

    private String status;
}
