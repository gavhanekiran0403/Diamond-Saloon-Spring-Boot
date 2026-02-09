package com.diamond.saloon.service;

import java.util.List;

import com.diamond.saloon.dto.SaloonServiceDto;

public interface SaloonService {

    SaloonServiceDto createService(SaloonServiceDto serviceDto);

    SaloonServiceDto updateService(String serviceId, SaloonServiceDto serviceDto);

    List<SaloonServiceDto> getAllServices();

    SaloonServiceDto getServiceById(String serviceId);

    void deleteService(String serviceId);

    List<SaloonServiceDto> getServicesByCategory(String category);

    List<SaloonServiceDto> getActiveServices();
}
