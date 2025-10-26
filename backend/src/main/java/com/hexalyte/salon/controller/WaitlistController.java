package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.WaitlistDTO;
import com.hexalyte.salon.service.WaitlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/waitlist")
@CrossOrigin(origins = "*")
public class WaitlistController {

    @Autowired
    private WaitlistService waitlistService;

    // Add customer to waitlist
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<WaitlistDTO> addToWaitlist(@Valid @RequestBody WaitlistDTO waitlistDTO) {
        WaitlistDTO createdWaitlist = waitlistService.addToWaitlist(waitlistDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWaitlist);
    }

    // Get waitlist by staff
    @GetMapping("/staff/{staffId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<WaitlistDTO>> getWaitlistByStaff(@PathVariable Long staffId) {
        List<WaitlistDTO> waitlist = waitlistService.getWaitlistByStaff(staffId);
        return ResponseEntity.ok(waitlist);
    }

    // Get waitlist by branch
    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<WaitlistDTO>> getWaitlistByBranch(@PathVariable Long branchId) {
        List<WaitlistDTO> waitlist = waitlistService.getWaitlistByBranch(branchId);
        return ResponseEntity.ok(waitlist);
    }

    // Get waitlist by customer
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<WaitlistDTO>> getWaitlistByCustomer(@PathVariable Long customerId) {
        List<WaitlistDTO> waitlist = waitlistService.getWaitlistByCustomer(customerId);
        return ResponseEntity.ok(waitlist);
    }

    // Convert waitlist entry to appointment
    @PostMapping("/{waitlistId}/convert")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<Object> convertWaitlistToAppointment(
            @PathVariable Long waitlistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) String startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) String endTime) {
        
        try {
            Object appointment = waitlistService.convertWaitlistToAppointment(
                    waitlistId, appointmentDate, 
                    LocalTime.parse(startTime), LocalTime.parse(endTime));
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to convert waitlist: " + e.getMessage());
        }
    }

    // Notify waitlist customers about availability
    @PostMapping("/notify")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<Void> notifyWaitlistCustomers(
            @RequestParam Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) String startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) String endTime) {
        
        waitlistService.notifyWaitlistCustomers(staffId, date, 
                LocalTime.parse(startTime), LocalTime.parse(endTime));
        return ResponseEntity.noContent().build();
    }

    // Remove from waitlist
    @DeleteMapping("/{waitlistId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<Void> removeFromWaitlist(
            @PathVariable Long waitlistId,
            @RequestParam(required = false) String reason) {
        waitlistService.removeFromWaitlist(waitlistId, reason);
        return ResponseEntity.noContent().build();
    }

    // Get waitlist statistics
    @GetMapping("/stats/branch/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<WaitlistService.WaitlistStatsDTO> getWaitlistStats(@PathVariable Long branchId) {
        WaitlistService.WaitlistStatsDTO stats = waitlistService.getWaitlistStats(branchId);
        return ResponseEntity.ok(stats);
    }

    // Clean up expired entries
    @PostMapping("/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cleanupExpiredEntries() {
        waitlistService.cleanupExpiredEntries();
        return ResponseEntity.noContent().build();
    }
}
