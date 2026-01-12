package com.diamond.saloon.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.ServiceDto;
import com.diamond.saloon.model.Service;

@Component
public class ServiceMapper {

    @Autowired
    private ModelMapper modelMapper;

    // Convert DTO to Entity
    public Service dtoToEntity(ServiceDto serviceDto) {
        return modelMapper.map(serviceDto, Service.class);
    }

    // Convert Entity to DTO
    public ServiceDto entityToDto(Service service) {
        return modelMapper.map(service, ServiceDto.class);
    }
}
