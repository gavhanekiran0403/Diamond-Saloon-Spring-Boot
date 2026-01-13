package com.diamond.saloon.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.diamond.saloon.dto.SaloonServiceDto;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.SaloonServiceMapper;
import com.diamond.saloon.model.SaloonServiceEntity;
import com.diamond.saloon.repository.SaloonServiceRepository;
import com.diamond.saloon.service.SaloonService;

@Service
public class SaloonServiceImpl implements SaloonService {

    @Autowired
    private SaloonServiceRepository saloonServiceRepository;

    @Autowired
    private SaloonServiceMapper saloonServiceMapper;

    @Override
    public SaloonServiceDto createService(SaloonServiceDto serviceDto) {
        SaloonServiceEntity service = saloonServiceMapper.dtoToEntity(serviceDto);
        SaloonServiceEntity savedService = saloonServiceRepository.save(service);
        return saloonServiceMapper.entityToDto(savedService);
    }

    @Override
    public SaloonServiceDto updateService(String serviceId, SaloonServiceDto serviceDto) {
        SaloonServiceEntity service = saloonServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        service.setServiceName(serviceDto.getServiceName());
        service.setCategory(serviceDto.getCategory());
        service.setPrice(serviceDto.getPrice());
        service.setDurationMinutes(serviceDto.getDurationMinutes());
        service.setActive(serviceDto.isActive());

        SaloonServiceEntity updatedService = saloonServiceRepository.save(service);
        return saloonServiceMapper.entityToDto(updatedService);
    }

    @Override
    public List<SaloonServiceDto> getAllServices() {
        List<SaloonServiceEntity> services = saloonServiceRepository.findAll();
        if (services.isEmpty()) {
            throw new ResourceNotFoundException("No services found");
        }
        return services.stream()
                .map(saloonServiceMapper::entityToDto)
                .toList();
    }

    @Override
    public SaloonServiceDto getServiceById(String serviceId) {
        SaloonServiceEntity service = saloonServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        return saloonServiceMapper.entityToDto(service);
    }

    @Override
    public void deleteService(String serviceId) {
        SaloonServiceEntity service = saloonServiceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        saloonServiceRepository.delete(service);
    }

    @Override
    public List<SaloonServiceDto> getServicesByCategory(String category) {
        List<SaloonServiceEntity> services = saloonServiceRepository.findByCategory(category);
        if (services.isEmpty()) {
            throw new ResourceNotFoundException("No services found for category: " + category);
        }
        return services.stream()
                .map(saloonServiceMapper::entityToDto)
                .toList();
    }

    @Override
    public List<SaloonServiceDto> getActiveServices() {
        List<SaloonServiceEntity> services = saloonServiceRepository.findByIsActive(true);
        if (services.isEmpty()) {
            throw new ResourceNotFoundException("No active services found");
        }
        return services.stream()
                .map(saloonServiceMapper::entityToDto)
                .toList();
    }
}
