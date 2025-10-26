package com.hexalyte.salon.dto;

import com.hexalyte.salon.model.BillingItem;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BillingItemDTO {
    private Long id;
    private Long billId;
    private Long serviceId;
    private String serviceName;
    private String serviceCategory;
    private Long staffId;
    private String staffName;
    private String staffEmployeeCode;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
    private BigDecimal commissionAmount;
    private Boolean commissionGenerated;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // Constructors
    public BillingItemDTO() {}

    public BillingItemDTO(BillingItem billingItem) {
        this.id = billingItem.getId();
        this.billId = billingItem.getBilling().getBillId();
        this.serviceId = billingItem.getService().getId();
        this.serviceName = billingItem.getService().getName();
        this.serviceCategory = billingItem.getService().getCategory();
        this.staffId = billingItem.getStaff().getId();
        this.staffName = billingItem.getStaff().getFullName();
        this.staffEmployeeCode = billingItem.getStaff().getEmployeeCode();
        this.quantity = billingItem.getQuantity();
        this.unitPrice = billingItem.getUnitPrice();
        this.total = billingItem.getTotal();
        this.commissionAmount = billingItem.calculateCommission();
        this.commissionGenerated = billingItem.getCommissionGenerated();
        this.createdAt = billingItem.getCreatedAt();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
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

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffEmployeeCode() {
        return staffEmployeeCode;
    }

    public void setStaffEmployeeCode(String staffEmployeeCode) {
        this.staffEmployeeCode = staffEmployeeCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public Boolean getCommissionGenerated() {
        return commissionGenerated;
    }

    public void setCommissionGenerated(Boolean commissionGenerated) {
        this.commissionGenerated = commissionGenerated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
