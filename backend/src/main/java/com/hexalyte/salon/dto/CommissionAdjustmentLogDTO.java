package com.hexalyte.salon.dto;

import com.hexalyte.salon.model.CommissionAdjustmentLog;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CommissionAdjustmentLogDTO {
    private Long id;
    private Long commissionId;
    private BigDecimal oldAmount;
    private BigDecimal newAmount;
    private BigDecimal amountDifference;
    private String reason;
    private Long changedBy;
    private String changedByName;
    private String adjustmentType;
    private String changeDescription;
    private String ipAddress;
    private String userAgent;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime changedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // Constructors
    public CommissionAdjustmentLogDTO() {}

    public CommissionAdjustmentLogDTO(CommissionAdjustmentLog log) {
        this.id = log.getId();
        this.commissionId = log.getCommission() != null ? log.getCommission().getId() : null;
        this.oldAmount = log.getOldAmount();
        this.newAmount = log.getNewAmount();
        this.amountDifference = log.getAmountDifference();
        this.reason = log.getReason();
        this.changedBy = log.getChangedBy();
        this.adjustmentType = log.getAdjustmentType() != null ? log.getAdjustmentType().name() : null;
        this.changeDescription = log.getChangeDescription();
        this.ipAddress = log.getIpAddress();
        this.userAgent = log.getUserAgent();
        this.changedAt = log.getChangedAt();
        this.createdAt = log.getCreatedAt();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(Long commissionId) {
        this.commissionId = commissionId;
    }

    public BigDecimal getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(BigDecimal oldAmount) {
        this.oldAmount = oldAmount;
    }

    public BigDecimal getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(BigDecimal newAmount) {
        this.newAmount = newAmount;
    }

    public BigDecimal getAmountDifference() {
        return amountDifference;
    }

    public void setAmountDifference(BigDecimal amountDifference) {
        this.amountDifference = amountDifference;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Long changedBy) {
        this.changedBy = changedBy;
    }

    public String getChangedByName() {
        return changedByName;
    }

    public void setChangedByName(String changedByName) {
        this.changedByName = changedByName;
    }

    public String getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public String getChangeDescription() {
        return changeDescription;
    }

    public void setChangeDescription(String changeDescription) {
        this.changeDescription = changeDescription;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
