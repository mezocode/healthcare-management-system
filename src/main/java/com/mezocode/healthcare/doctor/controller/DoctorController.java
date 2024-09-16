package com.mezocode.healthcare.doctor.controller;

import com.mezocode.healthcare.doctor.dto.DoctorDto;
import com.mezocode.healthcare.doctor.service.DoctorService;
import com.mezocode.healthcare.patient.domain.Patient;
import com.mezocode.healthcare.response.Responseable;
import com.mezocode.healthcare.response.RestResponse;
import com.mezocode.healthcare.shared.annotation.LogExecution;
import com.mezocode.healthcare.shared.annotation.Loggable;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.mezocode.healthcare.response.RestCollector.toResponseBody;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Slf4j
@Loggable(hideParameters = {"name"})
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    @LogExecution
    public ResponseEntity<RestResponse<Responseable>> getAllDoctors() {
        Page<DoctorDto> allDoctors = doctorService.getAllDoctors();

        return toResponseBody(allDoctors, OK);
    }

    @PostMapping("/captcha")
    public String captcha(@RequestBody  RecaptchaResponse recaptchaResponse) {
        return doctorService.captcha(recaptchaResponse.getRecaptchaResponse());
    }


    @GetMapping("/{id}")
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
