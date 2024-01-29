package com.mezocode.healthcare.patient.domain;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "patient")
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    public void updateAddress(Address newAddress) {
        if (newAddress.isValid()) {
            this.address = newAddress;
        } else {
            throw new InvalidAddressException("Invalid address");
        }
    }

    public void updateMedicalHistory(MedicalRecord newRecord) {
        this.medicalRecords.add(newRecord);
    }
}

