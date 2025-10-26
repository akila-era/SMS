package com.hexalyte.salon.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import com.hexalyte.salon.model.Service;

import java.math.BigDecimal;

public class ServiceBranchConfigurationDTO {
    
    private Long id;
    private Long serviceId;
    private String serviceName;
    private Long branchId;
    private String branchName;
    
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;
    
    @Min(value = 1)
    private Integer durationMinutes;
    
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal commissionRate;
    
    private Service.CommissionType commissionType = Service.CommissionType.PERCENTAGE;
    private BigDecimal fixedCommissionAmount = BigDecimal.ZERO;
    private Boolean isEnabled = true;
    private String requiredSkills;
    private String resourceRequirements;
    private String createdAt;
    private String updatedAt;

    // Constructors
    public ServiceBranchConfigurationDTO() {}

    public ServiceBranchConfigurationDTO(Long serviceId, Long branchId, BigDecimal price, Integer durationMinutes) {
        this.serviceId = serviceId;
        this.branchId = branchId;
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
}
