package com.mezocode.healthcare.doctor.repository;

import com.mezocode.healthcare.appointment.domain.Appointment;
import com.mezocode.healthcare.doctor.domain.Doctor;
import com.mezocode.healthcare.patient.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
