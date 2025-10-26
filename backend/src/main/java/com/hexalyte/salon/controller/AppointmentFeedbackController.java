package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.AppointmentFeedbackDTO;
import com.hexalyte.salon.service.AppointmentFeedbackService;
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
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class AppointmentFeedbackController {

    @Autowired
    private AppointmentFeedbackService feedbackService;

    // Submit feedback
    @PostMapping
    public ResponseEntity<AppointmentFeedbackDTO> submitFeedback(@Valid @RequestBody AppointmentFeedbackDTO feedbackDTO) {
        AppointmentFeedbackDTO createdFeedback = feedbackService.submitFeedback(feedbackDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFeedback);
    }

    // Get feedback by appointment
    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<AppointmentFeedbackDTO> getFeedbackByAppointment(@PathVariable Long appointmentId) {
        AppointmentFeedbackDTO feedback = feedbackService.getFeedbackByAppointment(appointmentId);
        return ResponseEntity.ok(feedback);
    }

    // Get feedback by staff
    @GetMapping("/staff/{staffId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentFeedbackDTO>> getFeedbackByStaff(@PathVariable Long staffId) {
        List<AppointmentFeedbackDTO> feedback = feedbackService.getFeedbackByStaff(staffId);
        return ResponseEntity.ok(feedback);
    }

    // Get feedback by branch
    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentFeedbackDTO>> getFeedbackByBranch(@PathVariable Long branchId) {
        List<AppointmentFeedbackDTO> feedback = feedbackService.getFeedbackByBranch(branchId);
        return ResponseEntity.ok(feedback);
    }

    // Get feedback by date range
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentFeedbackDTO>> getFeedbackByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AppointmentFeedbackDTO> feedback = feedbackService.getFeedbackByDateRange(startDate, endDate);
        return ResponseEntity.ok(feedback);
    }

    // Get recent feedback
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentFeedbackDTO>> getRecentFeedback() {
        List<AppointmentFeedbackDTO> feedback = feedbackService.getRecentFeedback();
        return ResponseEntity.ok(feedback);
    }

    // Get feedback statistics
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<AppointmentFeedbackService.FeedbackStatsDTO> getFeedbackStats(
            @RequestParam(required = false) Long branchId) {
        AppointmentFeedbackService.FeedbackStatsDTO stats = feedbackService.getFeedbackStats(branchId);
        return ResponseEntity.ok(stats);
    }

    // Update feedback
    @PutMapping("/{feedbackId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<AppointmentFeedbackDTO> updateFeedback(
            @PathVariable Long feedbackId, 
            @Valid @RequestBody AppointmentFeedbackDTO feedbackDTO) {
        AppointmentFeedbackDTO updatedFeedback = feedbackService.updateFeedback(feedbackId, feedbackDTO);
        return ResponseEntity.ok(updatedFeedback);
    }

    // Delete feedback
    @DeleteMapping("/{feedbackId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.noContent().build();
    }
}
