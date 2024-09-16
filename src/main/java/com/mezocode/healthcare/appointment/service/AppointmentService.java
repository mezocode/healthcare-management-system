package com.mezocode.healthcare.appointment.service;

import com.mezocode.healthcare.appointment.domain.Appointment;
import com.mezocode.healthcare.appointment.dto.AppointmentDto;
import com.mezocode.healthcare.appointment.repository.AppointmentRepository;
import com.mezocode.healthcare.doctor.domain.Doctor;
import com.mezocode.healthcare.doctor.repository.DoctorRepository;
import com.mezocode.healthcare.exception.AppointmentNotFoundException;
import com.mezocode.healthcare.exception.DoctorNotFoundException;
import com.mezocode.healthcare.exception.PatientNotFoundException;
import com.mezocode.healthcare.patient.domain.Patient;
import com.mezocode.healthcare.patient.repository.PatientRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    // Constructor for dependency injection

    public AppointmentDto scheduleAppointment(AppointmentDto appointmentDto) {
        Doctor doctor = doctorRepository
                .findById(appointmentDto.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor with id " + appointmentDto.getDoctorId() + " not found"));

        Patient patient = patientRepository.findById(appointmentDto.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient with id " + appointmentDto.getPatientId() + " not found"));

        Appointment appointment = doctor.scheduleAppointment(patient, appointmentDto.getAppointmentTime());

        // Save the new appointment to the repository
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Convert the saved Appointment entity to AppointmentDto
        return convertToDto(savedAppointment);
    }

    private AppointmentDto convertToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setPatientId(appointment.getPatient().getId());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setStatus(appointment.getStatus());

        // Set other fields as necessary
        return dto;
    }

    public Appointment rescheduleAppointment(Long appointmentId, AppointmentDto updatedAppointment) {
        Appointment appointmentById = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with id " + appointmentId + " not found"));

        // Check if the doctor is available at the new time...
        Doctor doctor = appointmentById.getDoctor();
        Patient patient = patientRepository.findById(updatedAppointment.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient with id " + updatedAppointment.getPatientId() + " not found"));
        Appointment appointment = doctor.scheduleAppointment(patient, updatedAppointment.getAppointmentTime());

        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with id " + appointmentId + " not found"));

        // Update the appointment status to "Cancelled" and save
        appointment.setStatus("Cancelled");
        return appointmentRepository.save(appointment);
    }

    public List<AppointmentDto> getAppointmentsForPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient with id " + patientId + " not found"));

        // Fetch and return all appointments for the given patient
        return appointmentRepository.findByPatient(patient)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<AppointmentDto> getAllAppointments() {

        Pageable pageable = PageRequest.of(0, 5);
        return appointmentRepository.findAll(pageable)
                .map(this::convertToDto);
    }
}
