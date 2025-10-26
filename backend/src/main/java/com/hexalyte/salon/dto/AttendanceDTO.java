package com.hexalyte.salon.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class AttendanceDTO {
    
    private Long id;
    
    @NotNull(message = "Staff is required")
    private Long staffId;
    private String staffName;
    
    @NotNull(message = "Work date is required")
    private LocalDate workDate;
    
    private LocalTime checkIn;
    private LocalTime checkOut;
    private BigDecimal totalHours;
    private String status;
    private String notes;
    private Boolean isLate;
    private BigDecimal overtimeHours;
    private String createdAt;
    private String updatedAt;

    // Constructors
    public AttendanceDTO() {}

    public AttendanceDTO(Long staffId, LocalDate workDate) {
        this.staffId = staffId;
        this.workDate = workDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public LocalTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalTime checkOut) {
        this.checkOut = checkOut;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsLate() {
        return isLate;
    }

    public void setIsLate(Boolean isLate) {
        this.isLate = isLate;
    }

    public BigDecimal getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(BigDecimal overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    public boolean isPresent() {
        return "PRESENT".equals(status) || "LATE".equals(status);
    }

    public boolean isAbsent() {
        return "ABSENT".equals(status);
    }

    public boolean isOnLeave() {
        return "LEAVE".equals(status);
    }

    public boolean isHalfDay() {
        return "HALF_DAY".equals(status);
    }

    public boolean isCheckedIn() {
        return checkIn != null && checkOut == null;
    }

    public boolean isCheckedOut() {
        return checkIn != null && checkOut != null;
    }

    public String getFormattedTotalHours() {
        if (totalHours == null) return "0h 0m";
        
        int hours = totalHours.intValue();
        int minutes = totalHours.subtract(BigDecimal.valueOf(hours)).multiply(BigDecimal.valueOf(60)).intValue();
        return hours + "h " + minutes + "m";
    }

    public String getFormattedOvertimeHours() {
        if (overtimeHours == null || overtimeHours.compareTo(BigDecimal.ZERO) == 0) return "0h 0m";
        
        int hours = overtimeHours.intValue();
        int minutes = overtimeHours.subtract(BigDecimal.valueOf(hours)).multiply(BigDecimal.valueOf(60)).intValue();
        return hours + "h " + minutes + "m";
    }
}
