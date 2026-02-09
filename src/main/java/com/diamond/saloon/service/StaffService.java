package com.diamond.saloon.service;

import java.util.List;

import com.diamond.saloon.dto.StaffDto;

public interface StaffService {

    StaffDto createStaff(StaffDto staffDto);

    StaffDto updateStaff(String staffId, StaffDto staffDto);

    List<StaffDto> getAllStaff();

    StaffDto getStaffById(String staffId);

    void deleteStaff(String staffId);

    List<StaffDto> getStaffBySpecialization(String specialization);

    List<StaffDto> getAvailableStaff();
}
