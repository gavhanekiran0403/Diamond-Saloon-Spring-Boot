package com.diamond.saloon.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.AppointmentDto;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.AppointmentMapper;
import com.diamond.saloon.model.Appointment;
import com.diamond.saloon.repository.AppointmentRepository;
import com.diamond.saloon.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public AppointmentDto createAppointment(AppointmentDto appointmentDto) {
        Appointment entity = appointmentMapper.dtoToEntity(appointmentDto);
        entity.setStatus("BOOKED");
        Appointment saved = appointmentRepository.save(entity);
        return appointmentMapper.entityToDto(saved);
    }

    @Override
    public AppointmentDto updateAppointment(String appointmentId, AppointmentDto appointmentDto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setTimeSlot(appointmentDto.getTimeSlot());
        appointment.setStatus(appointmentDto.getStatus());

        Appointment updated = appointmentRepository.save(appointment);
        return appointmentMapper.entityToDto(updated);
    }

    @Override
    public AppointmentDto getAppointmentById(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        return appointmentMapper.entityToDto(appointment);
    }

    @Override
    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        return appointments.stream()
                .map(appointmentMapper::entityToDto)
                .toList();
    }

    @Override
    public List<AppointmentDto> getAppointmentsByUserId(String userId) {
        List<Appointment> appointments = appointmentRepository.findByUserId(userId);
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found for user");
        }
        return appointments.stream()
                .map(appointmentMapper::entityToDto)
                .toList();
    }

    @Override
    public List<AppointmentDto> getAppointmentsByStatus(String status) {
        List<Appointment> appointments = appointmentRepository.findByStatus(status);
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found with status: " + status);
        }
        return appointments.stream()
                .map(appointmentMapper::entityToDto)
                .toList();
    }

    @Override
    public void cancelAppointment(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
    }
}
