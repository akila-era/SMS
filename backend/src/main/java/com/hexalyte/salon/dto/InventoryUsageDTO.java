package com.hexalyte.salon.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryUsageDTO {
    
    private Long id;
    
    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;
    
    @NotNull(message = "Service ID is required")
    private Long serviceId;
    
    @NotNull(message = "Staff ID is required")
    private Long staffId;
    
    @NotNull(message = "Branch ID is required")
    private Long branchId;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Quantity used is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Quantity used must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Quantity used must have at most 8 integer digits and 2 decimal places")
    private BigDecimal quantityUsed;
    
    @NotNull(message = "Unit cost is required")
    @DecimalMin(value = "0.0", message = "Unit cost must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Unit cost must have at most 8 integer digits and 2 decimal places")
    private BigDecimal unitCost;
    
    @NotNull(message = "Total cost is required")
    @DecimalMin(value = "0.0", message = "Total cost must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Total cost must have at most 8 integer digits and 2 decimal places")
    private BigDecimal totalCost;
    
    private LocalDateTime usedAt;
    
    // Additional fields for display
    private String productName;
    private String productCode;
    private String productUom;
    private String serviceName;
    private String staffName;
    private String branchName;
    private String customerName;
    private String appointmentDate;
    private String appointmentTime;
    
    // Constructors
    public InventoryUsageDTO() {}
    
    public InventoryUsageDTO(Long id, Long appointmentId, Long serviceId, Long staffId, Long branchId, 
                           Long productId, BigDecimal quantityUsed, BigDecimal unitCost, BigDecimal totalCost) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.serviceId = serviceId;
        this.staffId = staffId;
        this.branchId = branchId;
        this.productId = productId;
        this.quantityUsed = quantityUsed;
        this.unitCost = unitCost;
        this.totalCost = totalCost;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public Long getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
    
    public Long getStaffId() {
        return staffId;
    }
    
    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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
    
    public BigDecimal getQuantityUsed() {
        return quantityUsed;
    }
    
    public void setQuantityUsed(BigDecimal quantityUsed) {
        this.quantityUsed = quantityUsed;
    }
    
    public BigDecimal getUnitCost() {
        return unitCost;
    }
    
    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }
    
    public BigDecimal getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    
    public LocalDateTime getUsedAt() {
        return usedAt;
    }
    
    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
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
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getStaffName() {
        return staffName;
    }
    
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    
    public String getBranchName() {
        return branchName;
    }
    
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public String getAppointmentTime() {
        return appointmentTime;
    }
    
    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
