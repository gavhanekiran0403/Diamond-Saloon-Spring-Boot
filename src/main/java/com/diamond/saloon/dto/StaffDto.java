package com.diamond.saloon.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StaffDto {

    private String staffId;

    private String staffName;

    private String phone;

    private String specialization;

    private boolean isAvailable;
}
