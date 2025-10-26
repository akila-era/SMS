package com.hexalyte.salon.dto;

import com.hexalyte.salon.model.CommissionSummary;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CommissionSummaryDTO {
    private Long id;
    private Long staffId;
    private String staffName;
    private String staffEmployeeCode;
    private Long branchId;
    private String branchName;
    private String month;
    private Integer totalServices;
    private BigDecimal totalCommission;
    private BigDecimal averageCommissionPerService;
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime generatedAt;
    
    private Long approvedBy;
    private String approvedByName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lockedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // Constructors
    public CommissionSummaryDTO() {}

    public CommissionSummaryDTO(CommissionSummary summary) {
        this.id = summary.getId();
        this.staffId = summary.getStaff() != null ? summary.getStaff().getId() : null;
        this.staffName = summary.getStaff() != null ? summary.getStaff().getFullName() : null;
        this.staffEmployeeCode = summary.getStaff() != null ? summary.getStaff().getEmployeeCode() : null;
        this.branchId = summary.getBranch() != null ? summary.getBranch().getId() : null;
        this.branchName = summary.getBranch() != null ? summary.getBranch().getBranchName() : null;
        this.month = summary.getMonth();
        this.totalServices = summary.getTotalServices();
        this.totalCommission = summary.getTotalCommission();
        this.averageCommissionPerService = summary.getAverageCommissionPerService();
        this.status = summary.getStatus() != null ? summary.getStatus().name() : null;
        this.generatedAt = summary.getGeneratedAt();
        this.approvedBy = summary.getApprovedBy();
        this.approvedAt = summary.getApprovedAt();
        this.lockedAt = summary.getLockedAt();
        this.createdAt = summary.getCreatedAt();
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

    public String getStaffEmployeeCode() {
        return staffEmployeeCode;
    }

    public void setStaffEmployeeCode(String staffEmployeeCode) {
        this.staffEmployeeCode = staffEmployeeCode;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getTotalServices() {
        return totalServices;
    }

    public void setTotalServices(Integer totalServices) {
        this.totalServices = totalServices;
    }

    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(BigDecimal totalCommission) {
        this.totalCommission = totalCommission;
    }

    public BigDecimal getAverageCommissionPerService() {
        return averageCommissionPerService;
    }

    public void setAverageCommissionPerService(BigDecimal averageCommissionPerService) {
        this.averageCommissionPerService = averageCommissionPerService;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedByName() {
        return approvedByName;
    }

    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public LocalDateTime getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(LocalDateTime lockedAt) {
        this.lockedAt = lockedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
