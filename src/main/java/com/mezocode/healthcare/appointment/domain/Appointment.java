package com.mezocode.healthcare.appointment.domain;

import com.mezocode.healthcare.doctor.domain.Doctor;
import com.mezocode.healthcare.patient.domain.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private Patient patient;

    private LocalDateTime appointmentTime;

    private String status;

    // Getters and Setters

    public boolean reschedule(LocalDateTime newAppointmentTime) {
        if (doctor.isAvailable(newAppointmentTime)) {
            return false;
        }
        this.appointmentTime = newAppointmentTime;
        return true;
    }

    public void cancel() {
        this.status = "Cancelled";
    }

    public boolean isWithDoctor(Doctor doctor) {
        return this.doctor.equals(doctor);
    }

    public boolean isWithPatient(Patient patient) {
        return this.patient.equals(patient);
    }
}