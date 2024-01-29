package com.mezocode.healthcare.appointment.controller;

import com.mezocode.healthcare.appointment.domain.Appointment;
import com.mezocode.healthcare.appointment.service.AppointmentService;
import com.mezocode.healthcare.appointment.dto.AppointmentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping("/schedule")
    public Appointment scheduleAppointment(@RequestBody AppointmentDto appointmentDto) {
        return appointmentService.scheduleAppointment(appointmentDto);
    }

    @PutMapping("/{appointmentId}/reschedule")
    public Appointment rescheduleAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentDto updatedAppointment) {
        return appointmentService.rescheduleAppointment(appointmentId, updatedAppointment);
    }

    @PutMapping("/{appointmentId}/cancel")
    public Appointment cancelAppointment(@PathVariable Long appointmentId) {
        return appointmentService.cancelAppointment(appointmentId);
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getAppointmentsForPatient(@PathVariable Long patientId) {
        return appointmentService.getAppointmentsForPatient(patientId);
    }
}
