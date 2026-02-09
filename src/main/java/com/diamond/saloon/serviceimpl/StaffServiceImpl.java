package com.diamond.saloon.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.StaffDto;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.StaffMapper;
import com.diamond.saloon.model.Staff;
import com.diamond.saloon.repository.StaffRepository;
import com.diamond.saloon.service.StaffService;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StaffMapper staffMapper;

    @Override
    public StaffDto createStaff(StaffDto staffDto) {
        Staff entity = staffMapper.dtoToEntity(staffDto);
        return staffMapper.entityToDto(staffRepository.save(entity));
    }

    @Override
    public StaffDto updateStaff(String staffId, StaffDto staffDto) {
        Staff existing = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

        existing.setStaffName(staffDto.getStaffName());
        existing.setPhone(staffDto.getPhone());
        existing.setSpecialization(staffDto.getSpecialization());
        existing.setAvailable(staffDto.isAvailable());

        return staffMapper.entityToDto(staffRepository.save(existing));
    }

    @Override
    public List<StaffDto> getAllStaff() {
        List<Staff> staff = staffRepository.findAll();
        if (staff.isEmpty()) {
            throw new ResourceNotFoundException("No staff found");
        }
        return staff.stream().map(staffMapper::entityToDto).toList();
    }

    @Override
    public StaffDto getStaffById(String staffId) {
        Staff entity = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        return staffMapper.entityToDto(entity);
    }

    @Override
    public void deleteStaff(String staffId) {
        Staff entity = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        staffRepository.delete(entity);
    }

    @Override
    public List<StaffDto> getStaffBySpecialization(String specialization) {
        return staffRepository.findBySpecialization(specialization)
                .stream()
                .map(staffMapper::entityToDto)
                .toList();
    }

    @Override
    public List<StaffDto> getAvailableStaff() {
        return staffRepository.findByIsAvailable(true)
                .stream()
                .map(staffMapper::entityToDto)
                .toList();
    }
}
