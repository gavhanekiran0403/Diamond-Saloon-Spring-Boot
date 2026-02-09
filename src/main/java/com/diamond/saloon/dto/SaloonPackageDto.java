package com.diamond.saloon.dto;

import java.util.List;

import com.diamond.saloon.model.SaloonServiceEntity;

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
public class SaloonPackageDto {

    private String packageId;

    private String packageName;

    private String category; // MEN or WOMEN

    private String totalPrice;

    private List<SaloonServiceEntity> services;
}
