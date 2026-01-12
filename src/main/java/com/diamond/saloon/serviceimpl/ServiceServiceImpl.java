package com.diamond.saloon.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.ServiceDto;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.ServiceMapper;
import com.diamond.saloon.model.Service;
import com.diamond.saloon.repository.ServiceRepository;
import com.diamond.saloon.service.ServiceService;

@Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceMapper serviceMapper;

    @Override
    public ServiceDto createService(ServiceDto serviceDto) {
        Service serviceEntity = serviceMapper.dtoToEntity(serviceDto);
        Service savedService = serviceRepository.save(serviceEntity);
        return serviceMapper.entityToDto(savedService);
    }

    @Override
    public ServiceDto updateService(String serviceId, ServiceDto serviceDto) {
        Service serviceEntity = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service", "serviceId", serviceId));

        serviceEntity.setServiceName(serviceDto.getServiceName());
        serviceEntity.setCategory(serviceDto.getCategory());
        serviceEntity.setPrice(serviceDto.getPrice());
        serviceEntity.setDurationMinutes(serviceDto.getDurationMinutes());
        serviceEntity.setActive(serviceDto.isActive());

        Service updatedService = serviceRepository.save(serviceEntity);
        return serviceMapper.entityToDto(updatedService);
    }

    @Override
    public List<ServiceDto> getAllServices() {
        return serviceRepository.findAll()
                .stream()
                .map(serviceMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceDto getServiceById(String serviceId) {
        Service serviceEntity = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service", "serviceId", serviceId));

        return serviceMapper.entityToDto(serviceEntity);
    }

    @Override
    public void deleteService(String serviceId) {
        Service serviceEntity = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service", "serviceId", serviceId));

        serviceRepository.delete(serviceEntity);
    }

    @Override
    public List<ServiceDto> getServicesByCategory(String category) {
        return serviceRepository.findByCategory(category)
                .stream()
                .map(serviceMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDto> getActiveServices() {
        return serviceRepository.findByIsActive(true)
                .stream()
                .map(serviceMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
