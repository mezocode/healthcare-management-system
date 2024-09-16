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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private LocalDateTime appointmentTime;
    private String status;

    public void setDoctor(Doctor doctor) {
        if (this.doctor != null) {
            this.doctor.getAppointments().remove(this);
        }
        this.doctor = doctor;
        if (doctor != null) {
            doctor.getAppointments().add(this);
        }
    }

    public void setPatient(Patient patient) {
        if (this.patient != null) {
            this.patient.getAppointments().remove(this);
        }
        this.patient = patient;
        if (patient != null) {
            patient.getAppointments().add(this);
        }
    }
}
