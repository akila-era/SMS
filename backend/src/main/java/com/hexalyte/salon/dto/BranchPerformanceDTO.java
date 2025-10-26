package com.hexalyte.salon.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BranchPerformanceDTO {
    
    private Long branchId;
    private String branchName;
    private String branchCode;
    private String city;
    private Integer totalAppointments;
    private BigDecimal totalRevenue;
    private BigDecimal averageBill;
    private BigDecimal commissionPaid;
    private Integer totalCustomers;
    private Integer totalStaff;
    private BigDecimal averageRating;
    private LocalDate reportDate;
    private String period; // "daily", "weekly", "monthly", "yearly"

    // Constructors
    public BranchPerformanceDTO() {}

    public BranchPerformanceDTO(Long branchId, String branchName, String branchCode) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.branchCode = branchCode;
    }

    // Getters and Setters
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

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(Integer totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getAverageBill() {
        return averageBill;
    }

    public void setAverageBill(BigDecimal averageBill) {
        this.averageBill = averageBill;
    }

    public BigDecimal getCommissionPaid() {
        return commissionPaid;
    }

    public void setCommissionPaid(BigDecimal commissionPaid) {
        this.commissionPaid = commissionPaid;
    }

    public Integer getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(Integer totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public Integer getTotalStaff() {
        return totalStaff;
    }

    public void setTotalStaff(Integer totalStaff) {
        this.totalStaff = totalStaff;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
