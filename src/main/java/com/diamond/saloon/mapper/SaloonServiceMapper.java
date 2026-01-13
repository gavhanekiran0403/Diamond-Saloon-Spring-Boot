package com.diamond.saloon.mapper;

import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.SaloonServiceDto;
import com.diamond.saloon.model.SaloonServiceEntity;

@Component
public class SaloonServiceMapper {

    public SaloonServiceEntity dtoToEntity(SaloonServiceDto dto) {
        if (dto == null) return null;

        SaloonServiceEntity entity = new SaloonServiceEntity();
        entity.setServiceId(dto.getServiceId());
        entity.setServiceName(dto.getServiceName());
        entity.setCategory(dto.getCategory());
        entity.setPrice(dto.getPrice());
        entity.setDurationMinutes(dto.getDurationMinutes());
        entity.setActive(dto.isActive());

        return entity;
    }

    public SaloonServiceDto entityToDto(SaloonServiceEntity entity) {
        if (entity == null) return null;

        return new SaloonServiceDto(
                entity.getServiceId(),
                entity.getServiceName(),
                entity.getCategory(),
                entity.getPrice(),
                entity.getDurationMinutes(),
                entity.isActive()
        );
    }
}
