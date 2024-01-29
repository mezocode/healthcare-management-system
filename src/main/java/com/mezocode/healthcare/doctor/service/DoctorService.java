package com.mezocode.healthcare.doctor.service;

import com.mezocode.healthcare.doctor.domain.Doctor;
import com.mezocode.healthcare.doctor.dto.DoctorDto;
import com.mezocode.healthcare.doctor.repository.DoctorRepository;
import com.mezocode.healthcare.patient.domain.Patient;
import com.mezocode.healthcare.appointment.domain.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final RecaptchaService recaptchaService;

    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Doctor with id " + id + " not found")
        );
    }

    public DoctorDto getDoctor(Long id, String name, String date) {
        return convertToDto(getDoctorById(id));
    }

    public DoctorDto createDoctor(DoctorDto doctorDto) {
        return convertToDto(doctorRepository.save(convertToEntity(doctorDto)));
    }

    public DoctorDto updateDoctor(Long id, DoctorDto doctorDto) {
        Doctor existingDoctor = getDoctorById(id);
        existingDoctor.setName(doctorDto.getName());
        return convertToDto(doctorRepository.save(existingDoctor));
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    public DoctorDto scheduleAppointment(Long id, Patient patient, LocalDateTime dateTime) {
        Doctor doctor = getDoctorById(id);
        doctor.scheduleAppointment(patient, dateTime);
        return convertToDto(doctorRepository.save(doctor));
    }

    private DoctorDto convertToDto(Doctor doctor) {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setId(doctor.getId());
        doctorDto.setName(doctor.getName());
        doctorDto.setSpecialization(doctor.getSpecialization());
        doctorDto.setYearsOfExperience(doctor.getYearsOfExperience());
        doctorDto.setContactInformation(doctor.getContactInformation());
        doctorDto.setWorkingHours(doctor.getWorkingHours());
        doctorDto.setAppointmentIds(doctor.getAppointments().stream().map(Appointment::getId).collect(Collectors.toList()));
        return doctorDto;
    }

    private Doctor convertToEntity(DoctorDto doctorDto) {
        Doctor doctor = new Doctor();
        doctor.setId(doctorDto.getId());
        doctor.setName(doctorDto.getName());
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setYearsOfExperience(doctorDto.getYearsOfExperience());
        doctor.setContactInformation(doctorDto.getContactInformation());
        doctor.setWorkingHours(doctorDto.getWorkingHours());
        // You need to convert appointmentIds to Appointment entities. This part is omitted here.
        return doctor;
    }

    public String captcha(String recaptchaResponse) {

        return recaptchaService.isResponseValid(recaptchaResponse);

    }

    public void verifyRecaptchaWithWebClient() {
//        WebClient webClient = WebClient.create();
//
//        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//        formData.add("secret", "6LeJaSEpAAAAADtI2ioE0Zr68PpxzMZ0w0jSPNsV");
//        formData.add("response", "6LeJaSEpAAAAAHXPu9l13-Dp5_3m-9zE15axW-4N");
//
//        String response = webClient.post()
//                .uri("https://www.google.com/recaptcha/api/siteverify")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .body(BodyInserters.fromFormData(formData))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        // print response body or handle it
//        System.out.println(response);
    }
}