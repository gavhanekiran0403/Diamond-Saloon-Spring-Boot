package com.diamond.saloon.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "appointments")
public class Appointment {

    @Id
    private String appointmentId;

    private String userId;      // Reference to USER

    private String serviceId;   // Reference to SERVICE (optional)

    private String packageId;   // Reference to PACKAGE (optional)

    private LocalDate appointmentDate;

    private String timeSlot;    // e.g. "10:00 AM - 11:00 AM"

    private String status;      // BOOKED / COMPLETED / CANCELLED
}
