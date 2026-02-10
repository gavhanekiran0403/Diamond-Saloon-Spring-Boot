package com.diamond.saloon.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.AppointmentDto;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.AppointmentMapper;
import com.diamond.saloon.model.Appointment;
import com.diamond.saloon.repository.AppointmentRepository;
import com.diamond.saloon.responsedto.AppointmentResponseDto;
import com.diamond.saloon.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public AppointmentResponseDto createAppointment(AppointmentDto appointmentDto) {

        Appointment appointment = appointmentMapper.toEntity(appointmentDto);
        appointment.setStatus("BOOKED");

        return appointmentMapper.toDto(
                appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponseDto updateAppointment(
            String appointmentId, AppointmentDto appointmentDto) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setTimeSlot(appointmentDto.getTimeSlot());
        appointment.setStatus(appointmentDto.getStatus());

        return appointmentMapper.toDto(
                appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponseDto getAppointmentById(String appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        return appointmentMapper.toDto(appointment);
    }

    @Override
    public List<AppointmentResponseDto> getAllAppointments() {

        return appointmentRepository.findAll()
                .stream()
                .map(appointmentMapper::toDto)
                .toList();
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByUserId(String userId) {

        return appointmentRepository.findByUserId(userId)
                .stream()
                .map(appointmentMapper::toDto)
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
