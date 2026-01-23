package com.diamond.saloon.model;

import java.util.List;

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
@Document(collection = "saloon_packages")
public class SaloonPackageEntity {

    @Id
    private String packageId;

    private String packageName;

    private String category; // MEN or WOMEN

    private String totalPrice;

    private List<SaloonServiceEntity> services; // Embedded services
}
