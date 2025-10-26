package com.hexalyte.salon.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LowStockAlertDTO {
    
    private Long id;
    
    @NotNull(message = "Branch ID is required")
    private Long branchId;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Current quantity is required")
    @DecimalMin(value = "0.0", message = "Current quantity must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Current quantity must have at most 8 integer digits and 2 decimal places")
    private BigDecimal currentQuantity;
    
    @NotNull(message = "Alert quantity is required")
    @DecimalMin(value = "0.0", message = "Alert quantity must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Alert quantity must have at most 8 integer digits and 2 decimal places")
    private BigDecimal alertQuantity;
    
    @NotNull(message = "Alert type is required")
    private String alertType;
    
    private Boolean isResolved = false;
    
    private LocalDateTime resolvedAt;
    
    private Long resolvedBy;
    
    private LocalDateTime createdAt;
    
    // Additional fields for display
    private String productName;
    private String productCode;
    private String productUom;
    private String branchName;
    private String branchCode;
    private String resolvedByName;
    
    // Constructors
    public LowStockAlertDTO() {}
    
    public LowStockAlertDTO(Long id, Long branchId, Long productId, BigDecimal currentQuantity, 
                           BigDecimal alertQuantity, String alertType, Boolean isResolved) {
        this.id = id;
        this.branchId = branchId;
        this.productId = productId;
        this.currentQuantity = currentQuantity;
        this.alertQuantity = alertQuantity;
        this.alertType = alertType;
        this.isResolved = isResolved;
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
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public BigDecimal getCurrentQuantity() {
        return currentQuantity;
    }
    
    public void setCurrentQuantity(BigDecimal currentQuantity) {
        this.currentQuantity = currentQuantity;
    }
    
    public BigDecimal getAlertQuantity() {
        return alertQuantity;
    }
    
    public void setAlertQuantity(BigDecimal alertQuantity) {
        this.alertQuantity = alertQuantity;
    }
    
    public String getAlertType() {
        return alertType;
    }
    
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }
    
    public Boolean getIsResolved() {
        return isResolved;
    }
    
    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }
    
    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }
    
    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
    
    public Long getResolvedBy() {
        return resolvedBy;
    }
    
    public void setResolvedBy(Long resolvedBy) {
        this.resolvedBy = resolvedBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getProductUom() {
        return productUom;
    }
    
    public void setProductUom(String productUom) {
        this.productUom = productUom;
    }
    
    public String getBranchName() {
        return branchName;
    }
    
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    public String getBranchCode() {
        return branchCode;
    }
    
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }
    
    public String getResolvedByName() {
        return resolvedByName;
    }
    
    public void setResolvedByName(String resolvedByName) {
        this.resolvedByName = resolvedByName;
    }
}
