package com.mezocode.healthcare.doctor.domain;

import com.mezocode.healthcare.appointment.domain.Appointment;
import com.mezocode.healthcare.exception.DoctorNotAvailableException;
import com.mezocode.healthcare.patient.domain.Patient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    private int yearsOfExperience;
    private String contactInformation;
    private String workingHours;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    public boolean isAvailable(LocalDateTime dateTime) {
        LocalDateTime appointmentEnd = dateTime.plusHours(1);
        for (Appointment appointment : appointments) {
            LocalDateTime existingStart = appointment.getAppointmentTime();
            LocalDateTime existingEnd = existingStart.plusHours(1);

            // Check if the new appointment overlaps with any existing appointment
            if (!(dateTime.isAfter(existingEnd) || appointmentEnd.isBefore(existingStart))) {
                return false;  // Doctor is not available
            }
        }
        return true;  // Doctor is available
    }

    public Appointment scheduleAppointment(Patient patient, LocalDateTime dateTime) {
        if (!isAvailable(dateTime)) {
            throw new DoctorNotAvailableException("Doctor with id " + this.getId() + " is not available at the requested time");
        }
        Appointment appointment = new Appointment();
        appointment.setDoctor(this);
        appointment.setPatient(patient);
        appointment.setAppointmentTime(dateTime);
        appointment.setStatus("Scheduled");
        addAppointment(appointment);
        return appointment;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setDoctor(this);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.setDoctor(null);
    }
}
