package com.diamond.saloon.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "appointments")
@Data
public class Appointment {

    @Id
    private String appointmentId;

    private String userId;
    private String serviceId;     // optional
    private String packageId;     // optional

    private LocalDate appointmentDate;
    private String timeSlot;

    private String status;        // BOOKED / COMPLETED / CANCELLED
}
