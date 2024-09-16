package com.mezocode.healthcare.appointment.controller;

import com.mezocode.healthcare.appointment.domain.Appointment;
import com.mezocode.healthcare.appointment.dto.AppointmentDto;
import com.mezocode.healthcare.appointment.service.AppointmentService;
import com.mezocode.healthcare.shared.response.Responseable;
import com.mezocode.healthcare.shared.response.RestResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mezocode.healthcare.shared.response.RestCollector.toResponseBody;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping("/schedule")
    public ResponseEntity<RestResponse<Responseable>> scheduleAppointment(@RequestBody AppointmentDto appointmentDto) {
        AppointmentDto appointment = appointmentService.scheduleAppointment(appointmentDto);
        return toResponseBody(appointment, CREATED);
    }

    @PutMapping("/{appointmentId}/reschedule")
    public ResponseEntity<RestResponse<Responseable>> rescheduleAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentDto updatedAppointment) {
        Appointment appointment = appointmentService.rescheduleAppointment(appointmentId, updatedAppointment);
        return toResponseBody(appointment, CREATED);
    }

    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<RestResponse<Responseable>> cancelAppointment(@PathVariable Long appointmentId) {
        Appointment appointment = appointmentService.cancelAppointment(appointmentId);
        return toResponseBody(appointment, CREATED);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<RestResponse<Responseable>> getAppointmentsForPatient(@PathVariable Long patientId) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsForPatient(patientId);
        return toResponseBody(appointments, CREATED);
    }

    @GetMapping()
    public ResponseEntity<RestResponse<Responseable>> getAllAppointments(){
        Page<AppointmentDto> pageAppointments =  appointmentService.getAllAppointments();
        return toResponseBody(pageAppointments, OK);
    }
}
