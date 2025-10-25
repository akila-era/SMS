package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.*;
import com.hexalyte.salon.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Basic CRUD Operations
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<AppointmentDTO> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO createdAppointment = appointmentService.createAppointment(appointmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO updatedAppointment = appointmentService.updateAppointment(id, appointmentDTO);
        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    // Filter by Branch
    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByBranch(@PathVariable Long branchId) {
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByBranch(branchId);
        return ResponseEntity.ok(appointments);
    }

    // Filter by Staff
    @GetMapping("/staff/{staffId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByStaff(@PathVariable Long staffId) {
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByStaff(staffId);
        return ResponseEntity.ok(appointments);
    }

    // Filter by Customer
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByCustomer(@PathVariable Long customerId) {
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByCustomer(customerId);
        return ResponseEntity.ok(appointments);
    }

    // Filter by Date
    @GetMapping("/date/{date}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByDate(date);
        return ResponseEntity.ok(appointments);
    }

    // Today's Appointments
    @GetMapping("/today")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentDTO>> getTodaysAppointments() {
        List<AppointmentDTO> appointments = appointmentService.getTodaysAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/today/branch/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentDTO>> getTodaysAppointmentsByBranch(@PathVariable Long branchId) {
        List<AppointmentDTO> appointments = appointmentService.getTodaysAppointmentsByBranch(branchId);
        return ResponseEntity.ok(appointments);
    }

    // Upcoming Appointments
    @GetMapping("/upcoming")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentDTO>> getUpcomingAppointments(
            @RequestParam(defaultValue = "7") int days) {
        List<AppointmentDTO> appointments = appointmentService.getUpcomingAppointments(days);
        return ResponseEntity.ok(appointments);
    }

    // Status Management
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<AppointmentDTO> updateAppointmentStatus(
            @PathVariable Long id, @RequestParam String status) {
        AppointmentDTO updatedAppointment = appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(updatedAppointment);
    }

    // Cancel Appointment
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> cancelAppointment(
            @PathVariable Long id, @RequestParam(required = false) String reason) {
        AppointmentDTO cancelledAppointment = appointmentService.cancelAppointment(id, reason);
        return ResponseEntity.ok(cancelledAppointment);
    }

    // Reschedule Appointment
    @PatchMapping("/{id}/reschedule")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> rescheduleAppointment(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) String newStartTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) String newEndTime) {
        
        AppointmentDTO rescheduledAppointment = appointmentService.rescheduleAppointment(
                id, newDate, 
                java.time.LocalTime.parse(newStartTime), 
                java.time.LocalTime.parse(newEndTime));
        return ResponseEntity.ok(rescheduledAppointment);
    }

    // Availability Management
    @GetMapping("/availability/staff/{staffId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<AvailabilityDTO> getStaffAvailability(
            @PathVariable Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        AvailabilityDTO availability = appointmentService.getStaffAvailability(staffId, date);
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/availability/branch/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<AvailabilityDTO>> getBranchAvailability(
            @PathVariable Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AvailabilityDTO> availability = appointmentService.getBranchAvailability(branchId, date);
        return ResponseEntity.ok(availability);
    }
}


