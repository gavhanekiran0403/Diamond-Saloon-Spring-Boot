package com.diamond.saloon.service;

import java.util.List;

import com.diamond.saloon.dto.SaloonPackageDto;

public interface SaloonPackageService {

    SaloonPackageDto createPackage(SaloonPackageDto packageDto);

    SaloonPackageDto updatePackage(String packageId, SaloonPackageDto packageDto);

    List<SaloonPackageDto> getAllPackages();

    SaloonPackageDto getPackageById(String packageId);

    void deletePackage(String packageId);

    List<SaloonPackageDto> getPackagesByCategory(String category);
}
