package com.hexalyte.salon.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

public class BillingRequestDTO {
    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;
    
    @NotNull(message = "Branch ID is required")
    private Long branchId;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Discount amount must be non-negative")
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Loyalty redeemed must be non-negative")
    private BigDecimal loyaltyRedeemed = BigDecimal.ZERO;
    
    private String remarks;
    
    @NotEmpty(message = "Payment transactions are required")
    private List<PaymentRequestDTO> payments;

    // Constructors
    public BillingRequestDTO() {}

    public BillingRequestDTO(Long appointmentId, Long branchId, List<PaymentRequestDTO> payments) {
        this.appointmentId = appointmentId;
        this.branchId = branchId;
        this.payments = payments;
    }

    // Getters and Setters
    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getLoyaltyRedeemed() {
        return loyaltyRedeemed;
    }

    public void setLoyaltyRedeemed(BigDecimal loyaltyRedeemed) {
        this.loyaltyRedeemed = loyaltyRedeemed;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<PaymentRequestDTO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentRequestDTO> payments) {
        this.payments = payments;
    }
}
