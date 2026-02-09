package com.diamond.saloon.service;

import java.util.List;

import com.diamond.saloon.dto.AppointmentDto;

public interface AppointmentService {

    AppointmentDto createAppointment(AppointmentDto appointmentDto);

    AppointmentDto updateAppointment(String appointmentId, AppointmentDto appointmentDto);

    AppointmentDto getAppointmentById(String appointmentId);

    List<AppointmentDto> getAllAppointments();

    List<AppointmentDto> getAppointmentsByUserId(String userId);

    List<AppointmentDto> getAppointmentsByStatus(String status);

    void cancelAppointment(String appointmentId);
}
