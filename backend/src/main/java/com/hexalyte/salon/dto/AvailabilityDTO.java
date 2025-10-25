package com.hexalyte.salon.dto;

import java.time.LocalDate;
import java.util.List;

public class AvailabilityDTO {
    
    private Long staffId;
    private String staffName;
    private Long branchId;
    private String branchName;
    private LocalDate date;
    private List<TimeSlotDTO> availableSlots;
    private List<TimeSlotDTO> bookedSlots;

    // Constructors
    public AvailabilityDTO() {}

    public AvailabilityDTO(Long staffId, String staffName, Long branchId, String branchName, LocalDate date) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.branchId = branchId;
        this.branchName = branchName;
        this.date = date;
    }

    // Getters and Setters
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

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<TimeSlotDTO> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<TimeSlotDTO> availableSlots) {
        this.availableSlots = availableSlots;
    }

    public List<TimeSlotDTO> getBookedSlots() {
        return bookedSlots;
    }

    public void setBookedSlots(List<TimeSlotDTO> bookedSlots) {
        this.bookedSlots = bookedSlots;
    }
}


