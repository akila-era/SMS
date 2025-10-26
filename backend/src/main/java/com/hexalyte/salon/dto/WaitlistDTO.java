package com.hexalyte.salon.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class WaitlistDTO {
    
    private Long id;
    
    @NotNull(message = "Customer is required")
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    
    @NotNull(message = "Staff is required")
    private Long staffId;
    private String staffName;
    
    @NotNull(message = "Branch is required")
    private Long branchId;
    private String branchName;
    
    @NotNull(message = "Preferred date is required")
    private LocalDate preferredDate;
    
    @NotNull(message = "Preferred start time is required")
    private LocalTime preferredStartTime;
    
    @NotNull(message = "Preferred end time is required")
    private LocalTime preferredEndTime;
    
    @Min(value = 0, message = "Flexible days must be non-negative")
    private Integer flexibleDays = 0;
    
    @Min(value = 0, message = "Flexible hours must be non-negative")
    private Integer flexibleHours = 0;
    
    private String notes;
    private String status = "ACTIVE";
    private Integer priority = 0;
    private String createdAt;
    private String notifiedAt;
    private String expiresAt;

    // Constructors
    public WaitlistDTO() {}

    public WaitlistDTO(Long customerId, Long staffId, Long branchId, LocalDate preferredDate, 
                      LocalTime preferredStartTime, LocalTime preferredEndTime) {
        this.customerId = customerId;
        this.staffId = staffId;
        this.branchId = branchId;
        this.preferredDate = preferredDate;
        this.preferredStartTime = preferredStartTime;
        this.preferredEndTime = preferredEndTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
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

    public LocalDate getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(LocalDate preferredDate) {
        this.preferredDate = preferredDate;
    }

    public LocalTime getPreferredStartTime() {
        return preferredStartTime;
    }

    public void setPreferredStartTime(LocalTime preferredStartTime) {
        this.preferredStartTime = preferredStartTime;
    }

    public LocalTime getPreferredEndTime() {
        return preferredEndTime;
    }

    public void setPreferredEndTime(LocalTime preferredEndTime) {
        this.preferredEndTime = preferredEndTime;
    }

    public Integer getFlexibleDays() {
        return flexibleDays;
    }

    public void setFlexibleDays(Integer flexibleDays) {
        this.flexibleDays = flexibleDays;
    }

    public Integer getFlexibleHours() {
        return flexibleHours;
    }

    public void setFlexibleHours(Integer flexibleHours) {
        this.flexibleHours = flexibleHours;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getNotifiedAt() {
        return notifiedAt;
    }

    public void setNotifiedAt(String notifiedAt) {
        this.notifiedAt = notifiedAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
