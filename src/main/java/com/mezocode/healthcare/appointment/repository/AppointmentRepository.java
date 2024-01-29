package com.mezocode.healthcare.appointment.repository;

import com.mezocode.healthcare.appointment.domain.Appointment;
import com.mezocode.healthcare.patient.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatient(Patient patient);
}
