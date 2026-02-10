package com.diamond.saloon.responsedto;

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
public class AppointmentResponseDto {

	private String appointmentId;

    private String userId;
    
    private String fullName;

    private String serviceId;
    
    private String serviceName;

    private String packageId;
    
    private String packageName;

    private LocalDate appointmentDate;

    private String timeSlot;

    private String status;
}
