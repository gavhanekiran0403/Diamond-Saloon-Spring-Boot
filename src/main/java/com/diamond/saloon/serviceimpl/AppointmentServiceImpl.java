package com.diamond.saloon.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.AppointmentDto;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.AppointmentMapper;
import com.diamond.saloon.model.Appointment;
import com.diamond.saloon.model.SaloonPackageEntity;
import com.diamond.saloon.model.SaloonServiceEntity;
import com.diamond.saloon.model.User;
import com.diamond.saloon.repository.AppointmentRepository;
import com.diamond.saloon.repository.SaloonPackageRepository;
import com.diamond.saloon.repository.SaloonServiceRepository;
import com.diamond.saloon.repository.UserRepository;
import com.diamond.saloon.responsedto.AppointmentResponseDto;
import com.diamond.saloon.service.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private AppointmentMapper appointmentMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SaloonServiceRepository saloonServiceRepository;

	@Autowired
	private SaloonPackageRepository saloonPackageRepository;

	// ✅ Build FULL response with names
	private AppointmentResponseDto buildResponse(Appointment appointment) {

		AppointmentResponseDto dto = appointmentMapper.entityToDto(appointment);

		// USER
		userRepository.findById(appointment.getUserId()).ifPresent(user -> dto.setFullName(user.getFullName()));

		// SERVICE (optional)
		if (appointment.getServiceId() != null && !appointment.getServiceId().isBlank()) {

			saloonServiceRepository.findById(appointment.getServiceId())
					.ifPresent(service -> dto.setServiceName(service.getServiceName()));

		} else {
			dto.setServiceId(null);
			dto.setServiceName(null);
		}

		// PACKAGE (optional)
		if (appointment.getPackageId() != null && !appointment.getPackageId().isBlank()) {

			saloonPackageRepository.findById(appointment.getPackageId())
					.ifPresent(pkg -> dto.setPackageName(pkg.getPackageName()));

		} else {
			dto.setPackageId(null);
			dto.setPackageName(null);
		}

		return dto;
	}

	// CREATE
	@Override
	public AppointmentResponseDto createAppointment(AppointmentDto appointmentDto) {

		Appointment entity = appointmentMapper.dtoToEntity(appointmentDto);
		entity.setStatus("BOOKED");

		Appointment saved = appointmentRepository.save(entity);

		return buildResponse(saved);
	}

	// UPDATE
	@Override
	public AppointmentResponseDto updateAppointment(String appointmentId, AppointmentDto appointmentDto) {

		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

		appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
		appointment.setTimeSlot(appointmentDto.getTimeSlot());
		appointment.setStatus(appointmentDto.getStatus());

		Appointment updated = appointmentRepository.save(appointment);

		return buildResponse(updated);
	}

	// GET BY ID
	@Override
	public AppointmentResponseDto getAppointmentById(String appointmentId) {

		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

		return buildResponse(appointment); // ✅ FIXED
	}

	// GET ALL
	@Override
	public List<AppointmentResponseDto> getAllAppointments() {

		List<Appointment> appointments = appointmentRepository.findAll();

		if (appointments.isEmpty()) {
			throw new ResourceNotFoundException("No appointments found");
		}

		return appointments.stream().map(this::buildResponse) // ✅ FIXED
				.toList();
	}

	// GET BY USER
	@Override
	public List<AppointmentResponseDto> getAppointmentsByUserId(String userId) {

		List<Appointment> appointments = appointmentRepository.findByUserId(userId);

		if (appointments.isEmpty()) {
			throw new ResourceNotFoundException("No appointments found for user");
		}

		return appointments.stream().map(this::buildResponse) // ✅ FIXED
				.toList();
	}

	// GET BY STATUS
	@Override
	public List<AppointmentResponseDto> getAppointmentsByStatus(String status) {

		List<Appointment> appointments = appointmentRepository.findByStatus(status);

		if (appointments.isEmpty()) {
			throw new ResourceNotFoundException("No appointments found with status: " + status);
		}

		return appointments.stream().map(this::buildResponse) // ✅ FIXED
				.toList();
	}

	// CANCEL
	@Override
	public void cancelAppointment(String appointmentId) {

		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

		appointment.setStatus("CANCELLED");

		appointmentRepository.save(appointment);
	}
}
