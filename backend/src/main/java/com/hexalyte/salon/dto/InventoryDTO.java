package com.hexalyte.salon.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryDTO {
    
    private Long id;
    
    @NotNull(message = "Branch ID is required")
    private Long branchId;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.0", message = "Quantity must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Quantity must have at most 8 integer digits and 2 decimal places")
    private BigDecimal quantity;
    
    private LocalDateTime lastUpdated;
    
    // Additional fields for display
    private String productName;
    private String productCode;
    private String productCategory;
    private String productBrand;
    private String productUom;
    private String productType;
    private BigDecimal productCostPrice;
    private BigDecimal productAlertQuantity;
    private String branchName;
    private String branchCode;
    
    // Constructors
    public InventoryDTO() {}
    
    public InventoryDTO(Long id, Long branchId, Long productId, BigDecimal quantity, LocalDateTime lastUpdated) {
        this.id = id;
        this.branchId = branchId;
        this.productId = productId;
        this.quantity = quantity;
        this.lastUpdated = lastUpdated;
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
    
    public BigDecimal getQuantity() {
        return quantity;
    }
    
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
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
    
    public String getProductCategory() {
        return productCategory;
    }
    
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
    
    public String getProductBrand() {
        return productBrand;
    }
    
    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }
    
    public String getProductUom() {
        return productUom;
    }
    
    public void setProductUom(String productUom) {
        this.productUom = productUom;
    }
    
    public String getProductType() {
        return productType;
    }
    
    public void setProductType(String productType) {
        this.productType = productType;
    }
    
    public BigDecimal getProductCostPrice() {
        return productCostPrice;
    }
    
    public void setProductCostPrice(BigDecimal productCostPrice) {
        this.productCostPrice = productCostPrice;
    }
    
    public BigDecimal getProductAlertQuantity() {
        return productAlertQuantity;
    }
    
    public void setProductAlertQuantity(BigDecimal productAlertQuantity) {
        this.productAlertQuantity = productAlertQuantity;
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
}
