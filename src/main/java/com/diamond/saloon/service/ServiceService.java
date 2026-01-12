package com.diamond.saloon.service;

import java.util.List;

import com.diamond.saloon.dto.ServiceDto;

public interface ServiceService {

    ServiceDto createService(ServiceDto serviceDto);

    ServiceDto updateService(String serviceId, ServiceDto serviceDto);

    List<ServiceDto> getAllServices();

    ServiceDto getServiceById(String serviceId);

    void deleteService(String serviceId);

    List<ServiceDto> getServicesByCategory(String category);

    List<ServiceDto> getActiveServices();
}
