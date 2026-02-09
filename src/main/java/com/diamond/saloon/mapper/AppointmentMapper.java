package com.diamond.saloon.mapper;

import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.AppointmentDto;
import com.diamond.saloon.model.Appointment;

@Component
public class AppointmentMapper {

    public Appointment dtoToEntity(AppointmentDto dto) {
        if (dto == null) return null;

        Appointment entity = new Appointment();
        entity.setAppointmentId(dto.getAppointmentId());
        entity.setUserId(dto.getUserId());
        entity.setServiceId(dto.getServiceId());
        entity.setPackageId(dto.getPackageId());
        entity.setAppointmentDate(dto.getAppointmentDate());
        entity.setTimeSlot(dto.getTimeSlot());
        entity.setStatus(dto.getStatus());

        return entity;
    }

    public AppointmentDto entityToDto(Appointment entity) {
        if (entity == null) return null;

        return new AppointmentDto(
                entity.getAppointmentId(),
                entity.getUserId(),
                entity.getServiceId(),
                entity.getPackageId(),
                entity.getAppointmentDate(),
                entity.getTimeSlot(),
                entity.getStatus()
        );
    }
}
