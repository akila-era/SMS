package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "commission_summary")
@EntityListeners(AuditingEntityListener.class)
public class CommissionSummary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @NotBlank
    @Column(name = "month", length = 7) // Format: "2025-10"
    private String month;

    @Min(value = 0)
    @Column(name = "total_services")
    private Integer totalServices = 0;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "total_commission", precision = 10, scale = 2)
    private BigDecimal totalCommission = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SummaryStatus status = SummaryStatus.PENDING;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @Column(name = "approved_by")
    private Long approvedBy; // User ID who approved

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Enums
    public enum SummaryStatus {
        PENDING, APPROVED, LOCKED
    }

    // Constructors
    public CommissionSummary() {}

    public CommissionSummary(Staff staff, Branch branch, String month) {
        this.staff = staff;
        this.branch = branch;
        this.month = month;
        this.generatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getTotalServices() {
        return totalServices;
    }

    public void setTotalServices(Integer totalServices) {
        this.totalServices = totalServices;
    }

    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(BigDecimal totalCommission) {
        this.totalCommission = totalCommission;
    }

    public SummaryStatus getStatus() {
        return status;
    }

    public void setStatus(SummaryStatus status) {
        this.status = status;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public LocalDateTime getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(LocalDateTime lockedAt) {
        this.lockedAt = lockedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public boolean isPending() {
        return status == SummaryStatus.PENDING;
    }

    public boolean isApproved() {
        return status == SummaryStatus.APPROVED;
    }

    public boolean isLocked() {
        return status == SummaryStatus.LOCKED;
    }

    public void approve(Long approverId) {
        this.status = SummaryStatus.APPROVED;
        this.approvedBy = approverId;
        this.approvedAt = LocalDateTime.now();
    }

    public void lock() {
        this.status = SummaryStatus.LOCKED;
        this.lockedAt = LocalDateTime.now();
    }

    public void addCommission(BigDecimal commissionAmount) {
        this.totalCommission = this.totalCommission.add(commissionAmount);
        this.totalServices++;
    }

    public void removeCommission(BigDecimal commissionAmount) {
        this.totalCommission = this.totalCommission.subtract(commissionAmount);
        this.totalServices = Math.max(0, this.totalServices - 1);
    }

    public BigDecimal getAverageCommissionPerService() {
        if (totalServices == 0) {
            return BigDecimal.ZERO;
        }
        return totalCommission.divide(BigDecimal.valueOf(totalServices), 2, BigDecimal.ROUND_HALF_UP);
    }
}
