package com.diamond.saloon.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.AppointmentDto;
import com.diamond.saloon.model.Appointment;
import com.diamond.saloon.responsedto.AppointmentResponseDto;

@Component
public class AppointmentMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Appointment toEntity(AppointmentDto dto) {
        return modelMapper.map(dto, Appointment.class);
    }

    public AppointmentResponseDto toDto(Appointment appointment) {
        return modelMapper.map(appointment, AppointmentResponseDto.class);
    }
}
