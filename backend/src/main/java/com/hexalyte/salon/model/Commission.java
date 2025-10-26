package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "commission")
@EntityListeners(AuditingEntityListener.class)
public class Commission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Enumerated(EnumType.STRING)
    @Column(name = "commission_type", nullable = false)
    private CommissionType commissionType = CommissionType.PERCENT;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "rate", precision = 5, scale = 2)
    private BigDecimal rate;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "calculated_on")
    private LocalDateTime calculatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "is_manual", nullable = false)
    private Boolean isManual = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommissionStatus status = CommissionStatus.PENDING;

    @Column(name = "calculation_rule", columnDefinition = "TEXT")
    private String calculationRule; // Stores which rule was used (service/branch/staff level)

    @Column(name = "service_price", precision = 10, scale = 2)
    private BigDecimal servicePrice; // Store the service price at time of calculation

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Enums
    public enum CommissionType {
        PERCENT, FIXED, TIERED, SHARED
    }

    public enum CommissionStatus {
        PENDING, APPROVED, LOCKED, REVERSED
    }

    // Constructors
    public Commission() {}

    public Commission(Branch branch, Staff staff, Appointment appointment, Service service, 
                     CommissionType commissionType, BigDecimal rate, BigDecimal amount) {
        this.branch = branch;
        this.staff = staff;
        this.appointment = appointment;
        this.service = service;
        this.commissionType = commissionType;
        this.rate = rate;
        this.amount = amount;
        this.calculatedOn = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public CommissionType getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(CommissionType commissionType) {
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

    public LocalDateTime getCalculatedOn() {
        return calculatedOn;
    }

    public void setCalculatedOn(LocalDateTime calculatedOn) {
        this.calculatedOn = calculatedOn;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Boolean getIsManual() {
        return isManual;
    }

    public void setIsManual(Boolean isManual) {
        this.isManual = isManual;
    }

    public CommissionStatus getStatus() {
        return status;
    }

    public void setStatus(CommissionStatus status) {
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

    // Helper methods
    public boolean isApproved() {
        return status == CommissionStatus.APPROVED;
    }

    public boolean isLocked() {
        return status == CommissionStatus.LOCKED;
    }

    public boolean isReversed() {
        return status == CommissionStatus.REVERSED;
    }

    public boolean isPending() {
        return status == CommissionStatus.PENDING;
    }

    public void approve(User approver) {
        this.status = CommissionStatus.APPROVED;
        this.approvedBy = approver;
        this.approvedAt = LocalDateTime.now();
    }

    public void lock() {
        this.status = CommissionStatus.LOCKED;
    }

    public void reverse() {
        this.status = CommissionStatus.REVERSED;
    }

    // Convenience methods for compatibility
    public BigDecimal getCommissionRate() {
        return rate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.rate = commissionRate;
    }

    public void setCommissionDate(LocalDateTime commissionDate) {
        this.calculatedOn = commissionDate;
    }
}