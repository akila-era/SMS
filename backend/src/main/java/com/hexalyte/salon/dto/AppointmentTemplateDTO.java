package com.hexalyte.salon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AppointmentTemplateDTO {
    
    private Long id;
    
    @NotBlank(message = "Template name is required")
    private String templateName;
    
    private String description;
    
    @NotNull(message = "Branch is required")
    private Long branchId;
    private String branchName;
    
    private Integer estimatedDurationMinutes;
    private BigDecimal estimatedPrice;
    private Boolean isActive = true;
    private Integer usageCount = 0;
    private String createdAt;
    private String updatedAt;
    
    private List<AppointmentTemplateServiceDTO> templateServices;

    // Constructors
    public AppointmentTemplateDTO() {}

    public AppointmentTemplateDTO(String templateName, Long branchId) {
        this.templateName = templateName;
        this.branchId = branchId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getEstimatedDurationMinutes() {
        return estimatedDurationMinutes;
    }

    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) {
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }

    public BigDecimal getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(BigDecimal estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
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

    public List<AppointmentTemplateServiceDTO> getTemplateServices() {
        return templateServices;
    }

    public void setTemplateServices(List<AppointmentTemplateServiceDTO> templateServices) {
        this.templateServices = templateServices;
    }
}
