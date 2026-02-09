package com.diamond.saloon.mapper;

import org.springframework.stereotype.Component;

import com.diamond.saloon.dto.SaloonPackageDto;
import com.diamond.saloon.model.SaloonPackageEntity;

@Component
public class SaloonPackageMapper {

    public SaloonPackageEntity dtoToEntity(SaloonPackageDto dto) {
        if (dto == null) return null;

        SaloonPackageEntity entity = new SaloonPackageEntity();
        entity.setPackageId(dto.getPackageId());
        entity.setPackageName(dto.getPackageName());
        entity.setCategory(dto.getCategory());
        entity.setTotalPrice(dto.getTotalPrice());
        entity.setServices(dto.getServices());

        return entity;
    }

    public SaloonPackageDto entityToDto(SaloonPackageEntity entity) {
        if (entity == null) return null;

        return new SaloonPackageDto(
                entity.getPackageId(),
                entity.getPackageName(),
                entity.getCategory(),
                entity.getTotalPrice(),
                entity.getServices()
        );
    }
}
