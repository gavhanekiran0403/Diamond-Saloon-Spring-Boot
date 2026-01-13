package com.diamond.saloon.model;


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
@Document(collection = "services")
public class SaloonServiceEntity {


@Id
private String serviceId;


private String serviceName; 


private String category; // MEN or WOMEN


private String price; 


private String durationMinutes; 


private boolean isActive; // Availability status
}