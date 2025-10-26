package com.hexalyte.salon.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StaffCommissionSummaryDTO {
    
    private Long id;
    
    @NotNull(message = "Staff is required")
    private Long staffId;
    private String staffName;
    
    @NotNull(message = "Branch is required")
    private Long branchId;
    private String branchName;
    
    @NotNull(message = "Month is required")
    private String month;
    private String monthName;
    private String year;
    
    private Integer totalServices;
    private BigDecimal totalCommission;
    private BigDecimal baseSalary;
    private BigDecimal totalPayout;
    private BigDecimal totalRevenue;
    private BigDecimal averageRating;
    private Boolean isGenerated;
    private String createdAt;

    // Constructors
    public StaffCommissionSummaryDTO() {}

    public StaffCommissionSummaryDTO(Long staffId, Long branchId, String month) {
        this.staffId = staffId;
        this.branchId = branchId;
        this.month = month;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public void calculateTotalPayout() {
        if (baseSalary != null && totalCommission != null) {
            this.totalPayout = baseSalary.add(totalCommission);
        }
    }

    public String getFormattedTotalPayout() {
        if (totalPayout == null) return "LKR 0.00";
        return "LKR " + String.format("%,.2f", totalPayout);
    }

    public String getFormattedTotalCommission() {
        if (totalCommission == null) return "LKR 0.00";
        return "LKR " + String.format("%,.2f", totalCommission);
    }

    public String getFormattedBaseSalary() {
        if (baseSalary == null) return "LKR 0.00";
        return "LKR " + String.format("%,.2f", baseSalary);
    }

    public String getFormattedTotalRevenue() {
        if (totalRevenue == null) return "LKR 0.00";
        return "LKR " + String.format("%,.2f", totalRevenue);
    }

    public String getFormattedAverageRating() {
        if (averageRating == null) return "0.0";
        return String.format("%.1f", averageRating);
    }

    public String getDisplayMonth() {
        if (monthName != null && year != null) {
            return monthName + " " + year;
        }
        return month;
    }
}
