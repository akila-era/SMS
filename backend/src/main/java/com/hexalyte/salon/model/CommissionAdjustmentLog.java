package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "commission_adjustment_log")
@EntityListeners(AuditingEntityListener.class)
public class CommissionAdjustmentLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_id", nullable = false)
    private Commission commission;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "old_amount", precision = 10, scale = 2)
    private BigDecimal oldAmount;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "new_amount", precision = 10, scale = 2)
    private BigDecimal newAmount;

    @NotBlank
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @NotNull
    @Column(name = "changed_by")
    private Long changedBy; // User ID who made the change

    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "adjustment_type")
    private AdjustmentType adjustmentType;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Enums
    public enum AdjustmentType {
        MANUAL_ADJUSTMENT, BONUS, CORRECTION, REFUND, CANCELLATION, PRICE_CHANGE
    }

    // Constructors
    public CommissionAdjustmentLog() {}

    public CommissionAdjustmentLog(Commission commission, BigDecimal oldAmount, BigDecimal newAmount, 
                                 String reason, Long changedBy, AdjustmentType adjustmentType) {
        this.commission = commission;
        this.oldAmount = oldAmount;
        this.newAmount = newAmount;
        this.reason = reason;
        this.changedBy = changedBy;
        this.adjustmentType = adjustmentType;
        this.changedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Commission getCommission() {
        return commission;
    }

    public void setCommission(Commission commission) {
        this.commission = commission;
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

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public AdjustmentType getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(AdjustmentType adjustmentType) {
        this.adjustmentType = adjustmentType;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public BigDecimal getAmountDifference() {
        if (oldAmount == null || newAmount == null) {
            return BigDecimal.ZERO;
        }
        return newAmount.subtract(oldAmount);
    }

    public boolean isIncrease() {
        return getAmountDifference().compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isDecrease() {
        return getAmountDifference().compareTo(BigDecimal.ZERO) < 0;
    }

    public String getChangeDescription() {
        BigDecimal difference = getAmountDifference();
        if (difference.compareTo(BigDecimal.ZERO) > 0) {
            return String.format("Increased by LKR %.2f", difference);
        } else if (difference.compareTo(BigDecimal.ZERO) < 0) {
            return String.format("Decreased by LKR %.2f", difference.abs());
        } else {
            return "No change";
        }
    }
}
