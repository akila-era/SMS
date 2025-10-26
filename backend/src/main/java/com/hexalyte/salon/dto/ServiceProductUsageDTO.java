package com.hexalyte.salon.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public class ServiceProductUsageDTO {
    
    private Long id;
    private Long serviceId;
    private String serviceName;
    private String productName;
    private String productSku;
    
    @Min(value = 1)
    private Integer quantity = 1;
    
    @DecimalMin(value = "0.0", message = "Unit cost must be non-negative")
    private BigDecimal unitCost = BigDecimal.ZERO;
    
    private Boolean isRequired = true;
    private String notes;
    private String createdAt;
    private String updatedAt;
    private BigDecimal totalCost;

    // Constructors
    public ServiceProductUsageDTO() {}

    public ServiceProductUsageDTO(Long serviceId, String productName, Integer quantity, BigDecimal unitCost) {
        this.serviceId = serviceId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitCost = unitCost;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
