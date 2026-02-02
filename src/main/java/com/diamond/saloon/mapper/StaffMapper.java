package com.diamond.saloon.mapper;

import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.StaffDto;
import com.diamond.saloon.model.Staff;

@Component
public class StaffMapper {

    public Staff dtoToEntity(StaffDto dto) {
        if (dto == null) return null;

        return new Staff(
                dto.getStaffId(),
                dto.getStaffName(),
                dto.getPhone(),
                dto.getSpecialization(),
                dto.isAvailable()
        );
    }

    public StaffDto entityToDto(Staff entity) {
        if (entity == null) return null;

        return new StaffDto(
                entity.getStaffId(),
                entity.getStaffName(),
                entity.getPhone(),
                entity.getSpecialization(),
                entity.isAvailable()
        );
    }
}
