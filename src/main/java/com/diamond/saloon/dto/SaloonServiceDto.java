package com.diamond.saloon.dto;

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
public class SaloonServiceDto {

    private String serviceId;

    private String serviceName;

    private String category;   // MEN or WOMEN

    private String price;

    private String durationMinutes;

    private boolean isActive;  // Lombok will generate isActive() & setActive()
}
