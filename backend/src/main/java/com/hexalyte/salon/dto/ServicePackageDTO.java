package com.hexalyte.salon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.List;

public class ServicePackageDTO {
    
    private Long id;
    
    @NotBlank(message = "Package name is required")
    private String name;
    
    private String description;
    
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;
    
    @DecimalMin(value = "0.0", message = "Discount percentage must be non-negative")
    private BigDecimal discountPercentage = BigDecimal.ZERO;
    
    private Boolean isActive = true;
    private Integer totalDurationMinutes;
    private Boolean canSplitSessions = false;
    private Integer maxValidityDays;
    private String createdAt;
    private String updatedAt;
    private List<ServicePackageItemDTO> packageItems;
    private BigDecimal originalPrice;
    private BigDecimal discountAmount;

    // Constructors
    public ServicePackageDTO() {}

    public ServicePackageDTO(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
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

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getTotalDurationMinutes() {
        return totalDurationMinutes;
    }

    public void setTotalDurationMinutes(Integer totalDurationMinutes) {
        this.totalDurationMinutes = totalDurationMinutes;
    }

    public Boolean getCanSplitSessions() {
        return canSplitSessions;
    }

    public void setCanSplitSessions(Boolean canSplitSessions) {
        this.canSplitSessions = canSplitSessions;
    }

    public Integer getMaxValidityDays() {
        return maxValidityDays;
    }

    public void setMaxValidityDays(Integer maxValidityDays) {
        this.maxValidityDays = maxValidityDays;
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

    public List<ServicePackageItemDTO> getPackageItems() {
        return packageItems;
    }

    public void setPackageItems(List<ServicePackageItemDTO> packageItems) {
        this.packageItems = packageItems;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
}
