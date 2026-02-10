package com.diamond.saloon.service;

import java.util.List;

import com.diamond.saloon.dto.AppointmentDto;
import com.diamond.saloon.responsedto.AppointmentResponseDto;

public interface AppointmentService {

	AppointmentResponseDto createAppointment(AppointmentDto appointmentDto);

	AppointmentResponseDto updateAppointment(String appointmentId, AppointmentDto appointmentDto);

	AppointmentResponseDto getAppointmentById(String appointmentId);

    List<AppointmentResponseDto> getAllAppointments();

    List<AppointmentResponseDto> getAppointmentsByUserId(String userId);

    List<AppointmentResponseDto> getAppointmentsByStatus(String status);

    void cancelAppointment(String appointmentId);
}
