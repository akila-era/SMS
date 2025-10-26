package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_branch_configurations")
@EntityListeners(AuditingEntityListener.class)
public class ServiceBranchConfiguration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @Min(value = 1)
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "commission_rate")
    private BigDecimal commissionRate;

    @Column(name = "commission_type")
    @Enumerated(EnumType.STRING)
    private Service.CommissionType commissionType = Service.CommissionType.PERCENTAGE;

    @Column(name = "fixed_commission_amount")
    private BigDecimal fixedCommissionAmount = BigDecimal.ZERO;

    @Column(name = "is_enabled")
    private Boolean isEnabled = true;

    @Column(name = "required_skills", columnDefinition = "TEXT")
    private String requiredSkills;

    @Column(name = "resource_requirements", columnDefinition = "TEXT")
    private String resourceRequirements;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public ServiceBranchConfiguration() {}

    public ServiceBranchConfiguration(Service service, Branch branch, BigDecimal price, Integer durationMinutes) {
        this.service = service;
        this.branch = branch;
        this.price = price;
        this.durationMinutes = durationMinutes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Service.CommissionType getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(Service.CommissionType commissionType) {
        this.commissionType = commissionType;
    }

    public BigDecimal getFixedCommissionAmount() {
        return fixedCommissionAmount;
    }

    public void setFixedCommissionAmount(BigDecimal fixedCommissionAmount) {
        this.fixedCommissionAmount = fixedCommissionAmount;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getResourceRequirements() {
        return resourceRequirements;
    }

    public void setResourceRequirements(String resourceRequirements) {
        this.resourceRequirements = resourceRequirements;
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
    public BigDecimal getEffectiveCommission(BigDecimal servicePrice) {
        if (commissionType == Service.CommissionType.FIXED_AMOUNT) {
            return fixedCommissionAmount != null ? fixedCommissionAmount : BigDecimal.ZERO;
        } else {
            return servicePrice.multiply(commissionRate != null ? commissionRate : BigDecimal.ZERO);
        }
    }
}
