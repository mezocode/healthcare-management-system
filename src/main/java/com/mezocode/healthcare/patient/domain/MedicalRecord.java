package com.mezocode.healthcare.patient.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String diagnosis;
    private String treatment;
    private LocalDate treatmentDate;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
