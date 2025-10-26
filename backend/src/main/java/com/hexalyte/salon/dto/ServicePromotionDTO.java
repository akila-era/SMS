package com.hexalyte.salon.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ServicePromotionDTO {
    
    private Long id;
    private Long serviceId;
    private String serviceName;
    private Long servicePackageId;
    private String servicePackageName;
    
    @NotNull(message = "Promotion name is required")
    private String name;
    
    private String description;
    
    @DecimalMin(value = "0.0", message = "Discount percentage must be non-negative")
    private BigDecimal discountPercentage = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", message = "Discount amount must be non-negative")
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    private Boolean isActive = true;
    private Integer maxUses;
    private Integer usedCount = 0;
    private BigDecimal minServiceAmount;
    private String applicableBranches;
    private String createdAt;
    private String updatedAt;
    private Boolean isCurrentlyActive;
    private Boolean canBeUsed;

    // Constructors
    public ServicePromotionDTO() {}

    public ServicePromotionDTO(String name, LocalDate startDate, LocalDate endDate, BigDecimal discountPercentage) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountPercentage = discountPercentage;
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

    public Long getServicePackageId() {
        return servicePackageId;
    }

    public void setServicePackageId(Long servicePackageId) {
        this.servicePackageId = servicePackageId;
    }

    public String getServicePackageName() {
        return servicePackageName;
    }

    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public BigDecimal getMinServiceAmount() {
        return minServiceAmount;
    }

    public void setMinServiceAmount(BigDecimal minServiceAmount) {
        this.minServiceAmount = minServiceAmount;
    }

    public String getApplicableBranches() {
        return applicableBranches;
    }

    public void setApplicableBranches(String applicableBranches) {
        this.applicableBranches = applicableBranches;
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

    public Boolean getIsCurrentlyActive() {
        return isCurrentlyActive;
    }

    public void setIsCurrentlyActive(Boolean isCurrentlyActive) {
        this.isCurrentlyActive = isCurrentlyActive;
    }

    public Boolean getCanBeUsed() {
        return canBeUsed;
    }

    public void setCanBeUsed(Boolean canBeUsed) {
        this.canBeUsed = canBeUsed;
    }
}
