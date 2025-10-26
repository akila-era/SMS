package com.hexalyte.salon.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CommissionAdjustmentRequestDTO {
    
    @NotNull
    private Long commissionId;
    
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal newAmount;
    
    @NotBlank
    private String reason;
    
    private String adjustmentType;

    // Constructors
    public CommissionAdjustmentRequestDTO() {}

    public CommissionAdjustmentRequestDTO(Long commissionId, BigDecimal newAmount, String reason, String adjustmentType) {
        this.commissionId = commissionId;
        this.newAmount = newAmount;
        this.reason = reason;
        this.adjustmentType = adjustmentType;
    }

    // Getters and Setters
    public Long getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(Long commissionId) {
        this.commissionId = commissionId;
    }

    public BigDecimal getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(BigDecimal newAmount) {
        this.newAmount = newAmount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
    }
}
