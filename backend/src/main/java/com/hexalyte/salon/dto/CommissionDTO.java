package com.hexalyte.salon.dto;

import com.hexalyte.salon.model.Commission;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CommissionDTO {
    private Long id;
    private Long branchId;
    private String branchName;
    private Long staffId;
    private String staffName;
    private String staffEmployeeCode;
    private Long appointmentId;
    private Long serviceId;
    private String serviceName;
    private String commissionType;
    private BigDecimal rate;
    private BigDecimal amount;
    private String status;
    private String calculationRule;
    private BigDecimal servicePrice;
    private Boolean isManual;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime calculatedOn;
    
    private Long approvedBy;
    private String approvedByName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // Constructors
    public CommissionDTO() {}

    public CommissionDTO(Commission commission) {
        this.id = commission.getId();
        this.branchId = commission.getBranch() != null ? commission.getBranch().getId() : null;
        this.branchName = commission.getBranch() != null ? commission.getBranch().getBranchName() : null;
        this.staffId = commission.getStaff() != null ? commission.getStaff().getId() : null;
        this.staffName = commission.getStaff() != null ? commission.getStaff().getFullName() : null;
        this.staffEmployeeCode = commission.getStaff() != null ? commission.getStaff().getEmployeeCode() : null;
        this.appointmentId = commission.getAppointment() != null ? commission.getAppointment().getId() : null;
        this.serviceId = commission.getService() != null ? commission.getService().getId() : null;
        this.serviceName = commission.getService() != null ? commission.getService().getName() : null;
        this.commissionType = commission.getCommissionType() != null ? commission.getCommissionType().name() : null;
        this.rate = commission.getRate();
        this.amount = commission.getAmount();
        this.status = commission.getStatus() != null ? commission.getStatus().name() : null;
        this.calculationRule = commission.getCalculationRule();
        this.servicePrice = commission.getServicePrice();
        this.isManual = commission.getIsManual();
        this.calculatedOn = commission.getCalculatedOn();
        this.approvedBy = commission.getApprovedBy() != null ? commission.getApprovedBy().getId() : null;
        this.approvedByName = commission.getApprovedBy() != null ? commission.getApprovedBy().getUsername() : null;
        this.approvedAt = commission.getApprovedAt();
        this.createdAt = commission.getCreatedAt();
        this.updatedAt = commission.getUpdatedAt();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCalculationRule() {
        return calculationRule;
    }

    public void setCalculationRule(String calculationRule) {
        this.calculationRule = calculationRule;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }

    public Boolean getIsManual() {
        return isManual;
    }

    public void setIsManual(Boolean isManual) {
        this.isManual = isManual;
    }

    public LocalDateTime getCalculatedOn() {
        return calculatedOn;
    }

    public void setCalculatedOn(LocalDateTime calculatedOn) {
        this.calculatedOn = calculatedOn;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
