package com.hexalyte.salon.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDTO {
    
    private Long productId;
    
    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must not exceed 100 characters")
    private String name;
    
    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    private String code;
    
    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;
    
    @Size(max = 50, message = "Brand must not exceed 50 characters")
    private String brand;
    
    @Size(max = 20, message = "UOM must not exceed 20 characters")
    private String uom;
    
    @NotNull(message = "Product type is required")
    private String productType;
    
    @NotNull(message = "Cost price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Cost price must have at most 8 integer digits and 2 decimal places")
    private BigDecimal costPrice;
    
    @DecimalMin(value = "0.0", message = "Selling price must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Selling price must have at most 8 integer digits and 2 decimal places")
    private BigDecimal sellingPrice = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", message = "Alert quantity must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Alert quantity must have at most 8 integer digits and 2 decimal places")
    private BigDecimal alertQuantity = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", message = "Tax rate must be greater than or equal to 0")
    @DecimalMax(value = "100.0", message = "Tax rate must not exceed 100%")
    @Digits(integer = 3, fraction = 2, message = "Tax rate must have at most 3 integer digits and 2 decimal places")
    private BigDecimal taxRate = BigDecimal.ZERO;
    
    private String status = "ACTIVE";
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public ProductDTO() {}
    
    public ProductDTO(Long productId, String name, String code, String category, String brand, 
                     String uom, String productType, BigDecimal costPrice, BigDecimal sellingPrice, 
                     BigDecimal alertQuantity, BigDecimal taxRate, String status) {
        this.productId = productId;
        this.name = name;
        this.code = code;
        this.category = category;
        this.brand = brand;
        this.uom = uom;
        this.productType = productType;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.alertQuantity = alertQuantity;
        this.taxRate = taxRate;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getUom() {
        return uom;
    }
    
    public void setUom(String uom) {
        this.uom = uom;
    }
    
    public String getProductType() {
        return productType;
    }
    
    public void setProductType(String productType) {
        this.productType = productType;
    }
    
    public BigDecimal getCostPrice() {
        return costPrice;
    }
    
    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }
    
    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }
    
    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    
    public BigDecimal getAlertQuantity() {
        return alertQuantity;
    }
    
    public void setAlertQuantity(BigDecimal alertQuantity) {
        this.alertQuantity = alertQuantity;
    }
    
    public BigDecimal getTaxRate() {
        return taxRate;
    }
    
    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
