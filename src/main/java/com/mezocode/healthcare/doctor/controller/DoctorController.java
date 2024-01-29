package com.mezocode.healthcare.doctor.controller;

import com.mezocode.healthcare.doctor.dto.DoctorDto;
import com.mezocode.healthcare.doctor.service.DoctorService;
import com.mezocode.healthcare.patient.domain.Patient;
import com.mezocode.healthcare.shared.annotation.Loggable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Slf4j
@Loggable(hideParameters = {"name"})
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public List<DoctorDto> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @PostMapping("/captcha")
    public String captcha(@RequestBody  RecaptchaResponse recaptchaResponse) {
        log.info("Recaptcha response: " + recaptchaResponse.getRecaptchaResponse());
        return doctorService.captcha(recaptchaResponse.getRecaptchaResponse());
    }


    @GetMapping("/{id}")
    @Loggable(showParameters = {"name"})
    public DoctorDto getDoctor(@PathVariable Long id, @RequestParam String name, @RequestParam String date) {
        return doctorService.getDoctor(id, name, date);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDto createDoctor(@RequestBody DoctorDto doctorDto) {
        return doctorService.createDoctor(doctorDto);
    }

    @PutMapping("/{id}")
    public DoctorDto updateDoctor(@PathVariable Long id, @RequestBody DoctorDto doctorDto) {
        return doctorService.updateDoctor(id, doctorDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    @PostMapping("/{id}/schedule")
    public DoctorDto scheduleAppointment(@PathVariable Long id, @RequestBody Patient patient, LocalDateTime dateTime) {
        return doctorService.scheduleAppointment(id, patient, dateTime);
    }
}