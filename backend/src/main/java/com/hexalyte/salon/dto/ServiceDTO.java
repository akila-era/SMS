package com.hexalyte.salon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import com.hexalyte.salon.model.Service;

import java.math.BigDecimal;
import java.util.List;

public class ServiceDTO {
    
    private Long id;
    
    @NotBlank(message = "Service name is required")
    private String name;
    
    private String description;
    
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;
    
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;
    
    @DecimalMin(value = "0.0", message = "Commission rate must be non-negative")
    private BigDecimal commissionRate;
    
    private String category;
    private Boolean isActive = true;
    private String createdAt;
    private String updatedAt;
    private Integer appointmentCount;

    // New advanced fields
    @Min(value = 0, message = "Preparation buffer must be non-negative")
    private Integer preparationBufferMinutes = 0;

    @Min(value = 0, message = "Cleanup buffer must be non-negative")
    private Integer cleanupBufferMinutes = 0;

    private String defaultProducts;
    private Boolean isTaxable = true;
    private String requiredSkills;
    private String resourceRequirements;
    private Service.CommissionType commissionType = Service.CommissionType.PERCENTAGE;
    private BigDecimal fixedCommissionAmount = BigDecimal.ZERO;
    private Integer totalDurationMinutes;
    private List<ServiceBranchConfigurationDTO> branchConfigurations;
    private List<ServiceProductUsageDTO> productUsages;
    private List<ServicePromotionDTO> activePromotions;

    // Constructors
    public ServiceDTO() {}

    public ServiceDTO(String name, String description, BigDecimal price, Integer durationMinutes, BigDecimal commissionRate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.commissionRate = commissionRate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public Integer getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(Integer appointmentCount) {
        this.appointmentCount = appointmentCount;
    }

    // New advanced field getters and setters
    public Integer getPreparationBufferMinutes() {
        return preparationBufferMinutes;
    }

    public void setPreparationBufferMinutes(Integer preparationBufferMinutes) {
        this.preparationBufferMinutes = preparationBufferMinutes;
    }

    public Integer getCleanupBufferMinutes() {
        return cleanupBufferMinutes;
    }

    public void setCleanupBufferMinutes(Integer cleanupBufferMinutes) {
        this.cleanupBufferMinutes = cleanupBufferMinutes;
    }

    public String getDefaultProducts() {
        return defaultProducts;
    }

    public void setDefaultProducts(String defaultProducts) {
        this.defaultProducts = defaultProducts;
    }

    public Boolean getIsTaxable() {
        return isTaxable;
    }

    public void setIsTaxable(Boolean isTaxable) {
        this.isTaxable = isTaxable;
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

    public Integer getTotalDurationMinutes() {
        return totalDurationMinutes;
    }

    public void setTotalDurationMinutes(Integer totalDurationMinutes) {
        this.totalDurationMinutes = totalDurationMinutes;
    }

    public List<ServiceBranchConfigurationDTO> getBranchConfigurations() {
        return branchConfigurations;
    }

    public void setBranchConfigurations(List<ServiceBranchConfigurationDTO> branchConfigurations) {
        this.branchConfigurations = branchConfigurations;
    }

    public List<ServiceProductUsageDTO> getProductUsages() {
        return productUsages;
    }

    public void setProductUsages(List<ServiceProductUsageDTO> productUsages) {
        this.productUsages = productUsages;
    }

    public List<ServicePromotionDTO> getActivePromotions() {
        return activePromotions;
    }

    public void setActivePromotions(List<ServicePromotionDTO> activePromotions) {
        this.activePromotions = activePromotions;
    }
}


