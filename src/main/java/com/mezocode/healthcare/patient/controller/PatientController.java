package com.mezocode.healthcare.patient.controller;

import com.mezocode.healthcare.patient.domain.Address;
import com.mezocode.healthcare.patient.domain.MedicalRecord;
import com.mezocode.healthcare.patient.domain.Patient;
import com.mezocode.healthcare.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> registerPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.registerPatient(patient);
        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<Patient> updateAddress(@PathVariable Long id, @RequestBody Address newAddress) {
        Patient updatedPatient = patientService.updateAddress(id, newAddress);
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }

    @PutMapping("/{id}/medicalHistory")
    public ResponseEntity<Patient> updateMedicalHistory(@PathVariable Long id, @RequestBody MedicalRecord newRecord) {
        Patient updatedPatient = patientService.updateMedicalHistory(id, newRecord);
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }
}