package com.diamond.saloon.responsedto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AppointmentResponseDto {

    private String appointmentId;

   
    private String userId;
    private String userName;       

   
    private String serviceId;
    private String serviceName; 
    
 
    private String packageId;
    private String packageName;    

    private LocalDate appointmentDate;
    private String timeSlot;

    private String status;
}
