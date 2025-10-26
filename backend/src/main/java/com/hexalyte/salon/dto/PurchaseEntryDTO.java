package com.hexalyte.salon.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PurchaseEntryDTO {
    
    @NotNull(message = "Branch ID is required")
    private Long branchId;
    
    @NotNull(message = "Supplier ID is required")
    private Long supplierId;
    
    @Size(max = 100, message = "Reference number must not exceed 100 characters")
    private String referenceNumber;
    
    private String remarks;
    
    @NotNull(message = "Created by is required")
    private Long createdBy;
    
    @Valid
    @NotEmpty(message = "Purchase items are required")
    private List<PurchaseItemDTO> items;
    
    private LocalDateTime createdAt;
    
    // Additional fields for display
    private String supplierName;
    private String branchName;
    private String createdByName;
    private BigDecimal totalAmount;
    
    // Constructors
    public PurchaseEntryDTO() {}
    
    public PurchaseEntryDTO(Long branchId, Long supplierId, String referenceNumber, String remarks, 
                           Long createdBy, List<PurchaseItemDTO> items) {
        this.branchId = branchId;
        this.supplierId = supplierId;
        this.referenceNumber = referenceNumber;
        this.remarks = remarks;
        this.createdBy = createdBy;
        this.items = items;
    }
    
    // Getters and Setters
    public Long getBranchId() {
        return branchId;
    }
    
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
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
    
    public List<PurchaseItemDTO> getItems() {
        return items;
    }
    
    public void setItems(List<PurchaseItemDTO> items) {
        this.items = items;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getSupplierName() {
        return supplierName;
    }
    
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    
    public String getBranchName() {
        return branchName;
    }
    
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    public String getCreatedByName() {
        return createdByName;
    }
    
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    // Inner class for purchase items
    public static class PurchaseItemDTO {
        
        @NotNull(message = "Product ID is required")
        private Long productId;
        
        @NotNull(message = "Quantity is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "Quantity must have at most 8 integer digits and 2 decimal places")
        private BigDecimal quantity;
        
        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "Unit price must have at most 8 integer digits and 2 decimal places")
        private BigDecimal unitPrice;
        
        @DecimalMin(value = "0.0", message = "Total amount must be greater than or equal to 0")
        @Digits(integer = 8, fraction = 2, message = "Total amount must have at most 8 integer digits and 2 decimal places")
        private BigDecimal totalAmount;
        
        // Additional fields for display
        private String productName;
        private String productCode;
        private String productUom;
        
        // Constructors
        public PurchaseItemDTO() {}
        
        public PurchaseItemDTO(Long productId, BigDecimal quantity, BigDecimal unitPrice, BigDecimal totalAmount) {
            this.productId = productId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.totalAmount = totalAmount;
        }
        
        // Getters and Setters
        public Long getProductId() {
            return productId;
        }
        
        public void setProductId(Long productId) {
            this.productId = productId;
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
    }
}
