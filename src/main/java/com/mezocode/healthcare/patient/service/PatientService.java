package com.mezocode.healthcare.patient.service;

import com.mezocode.healthcare.exception.InvalidAddressException;
import com.mezocode.healthcare.exception.PatientNotFoundException;
import com.mezocode.healthcare.patient.domain.Address;
import com.mezocode.healthcare.patient.domain.MedicalRecord;
import com.mezocode.healthcare.patient.domain.Patient;
import com.mezocode.healthcare.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public Patient registerPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updateAddress(Long id, Address newAddress) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with id " + id + " not found"));
        if (!newAddress.isValid()) {
            throw new InvalidAddressException("Invalid address");
        }
        patient.updateAddress(newAddress);
        return patientRepository.save(patient);
    }

    public Patient updateMedicalHistory(Long id, MedicalRecord newRecord) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with id " + id + " not found"));
        patient.updateMedicalHistory(newRecord);
        return patientRepository.save(patient);
    }
}
