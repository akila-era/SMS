package com.hexalyte.salon.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServiceProductMappingDTO {
    
    private Long id;
    
    @NotNull(message = "Service ID is required")
    private Long serviceId;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Default quantity is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Default quantity must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Default quantity must have at most 8 integer digits and 2 decimal places")
    private BigDecimal defaultQuantity;
    
    private Boolean isRequired = true;
    
    private LocalDateTime createdAt;
    
    // Additional fields for display
    private String serviceName;
    private String productName;
    private String productCode;
    private String productUom;
    private String productType;
    
    // Constructors
    public ServiceProductMappingDTO() {}
    
    public ServiceProductMappingDTO(Long id, Long serviceId, Long productId, BigDecimal defaultQuantity, Boolean isRequired) {
        this.id = id;
        this.serviceId = serviceId;
        this.productId = productId;
        this.defaultQuantity = defaultQuantity;
        this.isRequired = isRequired;
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
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public BigDecimal getDefaultQuantity() {
        return defaultQuantity;
    }
    
    public void setDefaultQuantity(BigDecimal defaultQuantity) {
        this.defaultQuantity = defaultQuantity;
    }
    
    public Boolean getIsRequired() {
        return isRequired;
    }
    
    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
    
    public String getProductType() {
        return productType;
    }
    
    public void setProductType(String productType) {
        this.productType = productType;
    }
}
