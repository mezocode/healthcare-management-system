package com.mezocode.healthcare.doctor.dto;

import com.mezocode.healthcare.doctor.domain.Specialization;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class DoctorDto {
    private Long id;
    private String name;
    private Specialization specialization;
    private int yearsOfExperience;
    private String contactInformation;
    private String workingHours;
    private List<Long> appointmentIds;
}