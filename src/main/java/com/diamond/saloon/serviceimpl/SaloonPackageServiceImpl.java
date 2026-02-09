package com.diamond.saloon.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.SaloonPackageDto;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.SaloonPackageMapper;
import com.diamond.saloon.model.SaloonPackageEntity;
import com.diamond.saloon.repository.SaloonPackageRepository;
import com.diamond.saloon.service.SaloonPackageService;

@Service
public class SaloonPackageServiceImpl implements SaloonPackageService {

    @Autowired
    private SaloonPackageRepository saloonPackageRepository;

    @Autowired
    private SaloonPackageMapper saloonPackageMapper;

    @Override
    public SaloonPackageDto createPackage(SaloonPackageDto packageDto) {
        SaloonPackageEntity entity = saloonPackageMapper.dtoToEntity(packageDto);
        SaloonPackageEntity saved = saloonPackageRepository.save(entity);
        return saloonPackageMapper.entityToDto(saved);
    }

    @Override
    public SaloonPackageDto updatePackage(String packageId, SaloonPackageDto packageDto) {
        SaloonPackageEntity existing = saloonPackageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));

        existing.setPackageName(packageDto.getPackageName());
        existing.setCategory(packageDto.getCategory());
        existing.setServices(packageDto.getServices());
        existing.setTotalPrice(packageDto.getTotalPrice());

        SaloonPackageEntity updated = saloonPackageRepository.save(existing);
        return saloonPackageMapper.entityToDto(updated);
    }

    @Override
    public List<SaloonPackageDto> getAllPackages() {
        List<SaloonPackageEntity> packages = saloonPackageRepository.findAll();
        if (packages.isEmpty()) {
            throw new ResourceNotFoundException("No packages found");
        }
        return packages.stream()
                .map(saloonPackageMapper::entityToDto)
                .toList();
    }

    @Override
    public SaloonPackageDto getPackageById(String packageId) {
        SaloonPackageEntity entity = saloonPackageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
        return saloonPackageMapper.entityToDto(entity);
    }

    @Override
    public void deletePackage(String packageId) {
        SaloonPackageEntity entity = saloonPackageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
        saloonPackageRepository.delete(entity);
    }

    @Override
    public List<SaloonPackageDto> getPackagesByCategory(String category) {
        List<SaloonPackageEntity> packages = saloonPackageRepository.findByCategory(category);
        if (packages.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No packages found for category: " + category);
        }
        return packages.stream()
                .map(saloonPackageMapper::entityToDto)
                .toList();
    }
}
