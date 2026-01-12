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
public class Service {


@Id
private String serviceId; // _id (ObjectId)


private String serviceName; // Name of the service


private String category; // MEN or WOMEN


private double price; // Service cost


private int durationMinutes; // Service duration


private boolean isActive; // Availability status
}