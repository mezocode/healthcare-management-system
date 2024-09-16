package com.mezocode.healthcare.patient.domain;

import com.mezocode.healthcare.appointment.domain.Appointment;
import com.mezocode.healthcare.exception.InvalidAddressException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "patient")
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    public void updateAddress(Address newAddress) {
        if (newAddress.isValid()) {
            this.address = newAddress;
        } else {
            throw new InvalidAddressException("Invalid address");
        }
    }

    public void addMedicalRecord(MedicalRecord newRecord) {
        medicalRecords.add(newRecord);
        newRecord.setPatient(this);
    }

    public void removeMedicalRecord(MedicalRecord record) {
        medicalRecords.remove(record);
        record.setPatient(null);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setPatient(this);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.setPatient(null);
    }

    public void updateMedicalHistory(MedicalRecord newRecord) {
        this.medicalRecords.add(newRecord);
    }
}

