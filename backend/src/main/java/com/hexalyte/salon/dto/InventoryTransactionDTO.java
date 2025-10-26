package com.hexalyte.salon.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryTransactionDTO {
    
    private Long transactionId;
    
    @NotNull(message = "Branch ID is required")
    private Long branchId;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Transaction type is required")
    private String type;
    
    @NotNull(message = "Quantity is required")
    @Digits(integer = 8, fraction = 2, message = "Quantity must have at most 8 integer digits and 2 decimal places")
    private BigDecimal quantity;
    
    @DecimalMin(value = "0.0", message = "Unit price must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Unit price must have at most 8 integer digits and 2 decimal places")
    private BigDecimal unitPrice = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", message = "Total amount must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Total amount must have at most 8 integer digits and 2 decimal places")
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    private Long supplierId;
    
    @Size(max = 100, message = "Reference number must not exceed 100 characters")
    private String referenceNumber;
    
    private String remarks;
    
    @NotNull(message = "Created by is required")
    private Long createdBy;
    
    private LocalDateTime createdAt;
    
    // Additional fields for display
    private String productName;
    private String productCode;
    private String branchName;
    private String supplierName;
    private String createdByName;
    
    // Constructors
    public InventoryTransactionDTO() {}
    
    public InventoryTransactionDTO(Long transactionId, Long branchId, Long productId, String type, 
                                 BigDecimal quantity, BigDecimal unitPrice, BigDecimal totalAmount,
                                 Long supplierId, String referenceNumber, String remarks, Long createdBy) {
        this.transactionId = transactionId;
        this.branchId = branchId;
        this.productId = productId;
        this.type = type;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.supplierId = supplierId;
        this.referenceNumber = referenceNumber;
        this.remarks = remarks;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public BigDecimal getQuantity() {
        return quantity;
    }
    
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public Long getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
    
    public String getReferenceNumber() {
        return referenceNumber;
    }
    
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
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
    
    public String getBranchName() {
        return branchName;
    }
    
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    public String getSupplierName() {
        return supplierName;
    }
    
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    
    public String getCreatedByName() {
        return createdByName;
    }
    
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }
}
