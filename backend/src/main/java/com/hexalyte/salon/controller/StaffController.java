package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.StaffDTO;
import com.hexalyte.salon.dto.AttendanceDTO;
import com.hexalyte.salon.dto.StaffCommissionSummaryDTO;
import com.hexalyte.salon.service.StaffService;
import com.hexalyte.salon.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<StaffDTO>> getAllStaff() {
        List<StaffDTO> staff = staffService.getAllStaff();
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<StaffDTO>> getActiveStaff() {
        List<StaffDTO> staff = staffService.getActiveStaff();
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<StaffDTO>> getStaffByBranch(@PathVariable Long branchId) {
        List<StaffDTO> staff = staffService.getStaffByBranch(branchId);
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('BEAUTICIAN')")
    public ResponseEntity<StaffDTO> getStaffById(@PathVariable Long id) {
        StaffDTO staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<StaffDTO> createStaff(@Valid @RequestBody StaffDTO staffDTO) {
        StaffDTO createdStaff = staffService.createStaff(staffDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStaff);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<StaffDTO> updateStaff(@PathVariable Long id, @Valid @RequestBody StaffDTO staffDTO) {
        StaffDTO updatedStaff = staffService.updateStaff(id, staffDTO);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<StaffDTO> toggleStaffStatus(@PathVariable Long id) {
        StaffDTO updatedStaff = staffService.toggleStaffStatus(id);
        return ResponseEntity.ok(updatedStaff);
    }

    // Advanced Staff Management Endpoints
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<StaffDTO>> getStaffByStatus(@PathVariable String status) {
        List<StaffDTO> staff = staffService.getStaffByStatus(status);
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/designation/{designation}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<StaffDTO>> getStaffByDesignation(@PathVariable String designation) {
        List<StaffDTO> staff = staffService.getStaffByDesignation(designation);
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/skill/{skill}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<StaffDTO>> getStaffBySkill(@PathVariable String skill) {
        List<StaffDTO> staff = staffService.getStaffBySkill(skill);
        return ResponseEntity.ok(staff);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<StaffDTO> updateStaffStatus(@PathVariable Long id, @RequestParam String status) {
        StaffDTO updatedStaff = staffService.updateStaffStatus(id, status);
        return ResponseEntity.ok(updatedStaff);
    }

    // Commission Summary Endpoints
    @PostMapping("/{id}/commission-summary")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<StaffCommissionSummaryDTO> generateCommissionSummary(
            @PathVariable Long id, @RequestParam String month) {
        StaffCommissionSummaryDTO summary = staffService.generateCommissionSummary(id, month);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/commission-summary/month/{month}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<StaffCommissionSummaryDTO>> getCommissionSummariesByMonth(@PathVariable String month) {
        List<StaffCommissionSummaryDTO> summaries = staffService.getCommissionSummariesByMonth(month);
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/{id}/commission-summary")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<StaffCommissionSummaryDTO>> getCommissionSummariesByStaff(@PathVariable Long id) {
        List<StaffCommissionSummaryDTO> summaries = staffService.getCommissionSummariesByStaff(id);
        return ResponseEntity.ok(summaries);
    }

    // Attendance Endpoints
    @PostMapping("/{id}/check-in")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('BEAUTICIAN')")
    public ResponseEntity<AttendanceDTO> checkIn(@PathVariable Long id, @RequestParam(required = false) String date) {
        LocalDate workDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        AttendanceDTO attendance = attendanceService.checkIn(id, workDate);
        return ResponseEntity.ok(attendance);
    }

    @PostMapping("/{id}/check-out")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('BEAUTICIAN')")
    public ResponseEntity<AttendanceDTO> checkOut(@PathVariable Long id, @RequestParam(required = false) String date) {
        LocalDate workDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        AttendanceDTO attendance = attendanceService.checkOut(id, workDate);
        return ResponseEntity.ok(attendance);
    }

    @PostMapping("/{id}/mark-absent")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<AttendanceDTO> markAbsent(@PathVariable Long id, 
                                                   @RequestParam(required = false) String date,
                                                   @RequestParam(required = false) String notes) {
        LocalDate workDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        AttendanceDTO attendance = attendanceService.markAbsent(id, workDate, notes);
        return ResponseEntity.ok(attendance);
    }

    @PostMapping("/{id}/mark-leave")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<AttendanceDTO> markLeave(@PathVariable Long id, 
                                                  @RequestParam(required = false) String date,
                                                  @RequestParam(required = false) String notes) {
        LocalDate workDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        AttendanceDTO attendance = attendanceService.markLeave(id, workDate, notes);
        return ResponseEntity.ok(attendance);
    }

    @PostMapping("/{id}/mark-half-day")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<AttendanceDTO> markHalfDay(@PathVariable Long id, 
                                                    @RequestParam(required = false) String date,
                                                    @RequestParam(required = false) String notes) {
        LocalDate workDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        AttendanceDTO attendance = attendanceService.markHalfDay(id, workDate, notes);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/{id}/attendance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByStaff(@PathVariable Long id) {
        List<AttendanceDTO> attendance = attendanceService.getAttendanceByStaff(id);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/{id}/attendance/range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByStaffAndDateRange(
            @PathVariable Long id, 
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<AttendanceDTO> attendance = attendanceService.getAttendanceByStaffAndDateRange(id, start, end);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/attendance/date")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByDate(@RequestParam String date) {
        LocalDate workDate = LocalDate.parse(date);
        List<AttendanceDTO> attendance = attendanceService.getAttendanceByDate(workDate);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/attendance/range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByDateRange(
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<AttendanceDTO> attendance = attendanceService.getAttendanceByDateRange(start, end);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/attendance/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<AttendanceDTO>> getActiveCheckIns() {
        List<AttendanceDTO> activeCheckIns = attendanceService.getActiveCheckIns();
        return ResponseEntity.ok(activeCheckIns);
    }

    @GetMapping("/{id}/attendance/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('BEAUTICIAN')")
    public ResponseEntity<AttendanceDTO> getActiveCheckInByStaff(@PathVariable Long id) {
        AttendanceDTO attendance = attendanceService.getActiveCheckInByStaff(id);
        return ResponseEntity.ok(attendance);
    }

    @PutMapping("/attendance/{attendanceId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<AttendanceDTO> updateAttendance(@PathVariable Long attendanceId, 
                                                         @Valid @RequestBody AttendanceDTO attendanceDTO) {
        AttendanceDTO updatedAttendance = attendanceService.updateAttendance(attendanceId, attendanceDTO);
        return ResponseEntity.ok(updatedAttendance);
    }

    @DeleteMapping("/attendance/{attendanceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long attendanceId) {
        attendanceService.deleteAttendance(attendanceId);
        return ResponseEntity.noContent().build();
    }
}


