package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff_commission_summary")
@EntityListeners(AuditingEntityListener.class)
public class StaffCommissionSummary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @NotNull
    @Column(name = "month", length = 7)
    private String month; // Format: YYYY-MM

    @Column(name = "total_services")
    private Integer totalServices = 0;

    @Column(name = "total_commission", precision = 10, scale = 2)
    private BigDecimal totalCommission = BigDecimal.ZERO;

    @Column(name = "base_salary", precision = 10, scale = 2)
    private BigDecimal baseSalary = BigDecimal.ZERO;

    @Column(name = "total_payout", precision = 10, scale = 2)
    private BigDecimal totalPayout = BigDecimal.ZERO;

    @Column(name = "total_revenue", precision = 10, scale = 2)
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "is_generated")
    private Boolean isGenerated = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public StaffCommissionSummary() {}

    public StaffCommissionSummary(Staff staff, Branch branch, String month) {
        this.staff = staff;
        this.branch = branch;
        this.month = month;
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

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getTotalPayout() {
        return totalPayout;
    }

    public void setTotalPayout(BigDecimal totalPayout) {
        this.totalPayout = totalPayout;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public Boolean getIsGenerated() {
        return isGenerated;
    }

    public void setIsGenerated(Boolean isGenerated) {
        this.isGenerated = isGenerated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public void calculateTotalPayout() {
        this.totalPayout = this.baseSalary.add(this.totalCommission);
    }

    public String getStaffName() {
        return staff != null ? staff.getFullName() : "";
    }

    public String getBranchName() {
        return branch != null ? branch.getName() : "";
    }

    public String getYear() {
        return month != null && month.length() >= 4 ? month.substring(0, 4) : "";
    }

    public String getMonthName() {
        if (month == null || month.length() < 7) return "";
        
        String monthNum = month.substring(5, 7);
        String[] monthNames = {
            "", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        
        try {
            int monthIndex = Integer.parseInt(monthNum);
            return monthIndex >= 1 && monthIndex <= 12 ? monthNames[monthIndex] : "";
        } catch (NumberFormatException e) {
            return "";
        }
    }
}
