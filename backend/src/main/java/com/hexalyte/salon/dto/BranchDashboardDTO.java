package com.hexalyte.salon.dto;

import java.math.BigDecimal;
import java.util.List;

public class BranchDashboardDTO {
    
    private Long totalBranches;
    private Long activeBranches;
    private Long inactiveBranches;
    private BigDecimal totalRevenue;
    private BigDecimal monthlyRevenue;
    private Integer totalAppointments;
    private Integer totalCustomers;
    private Integer totalStaff;
    private BigDecimal averageRating;
    
    private List<BranchPerformanceDTO> topPerformingBranches;
    private List<BranchPerformanceDTO> recentActivity;
    private List<BranchPerformanceDTO> branchComparison;
    
    // Charts data
    private List<ChartDataDTO> revenueByBranch;
    private List<ChartDataDTO> appointmentsByBranch;
    private List<ChartDataDTO> monthlyTrend;
    private List<ChartDataDTO> branchShare;

    // Constructors
    public BranchDashboardDTO() {}

    // Getters and Setters
    public Long getTotalBranches() {
        return totalBranches;
    }

    public void setTotalBranches(Long totalBranches) {
        this.totalBranches = totalBranches;
    }

    public Long getActiveBranches() {
        return activeBranches;
    }

    public void setActiveBranches(Long activeBranches) {
        this.activeBranches = activeBranches;
    }

    public Long getInactiveBranches() {
        return inactiveBranches;
    }

    public void setInactiveBranches(Long inactiveBranches) {
        this.inactiveBranches = inactiveBranches;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public Integer getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(Integer totalAppointments) {
        this.totalAppointments = totalAppointments;
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

    public List<BranchPerformanceDTO> getTopPerformingBranches() {
        return topPerformingBranches;
    }

    public void setTopPerformingBranches(List<BranchPerformanceDTO> topPerformingBranches) {
        this.topPerformingBranches = topPerformingBranches;
    }

    public List<BranchPerformanceDTO> getRecentActivity() {
        return recentActivity;
    }

    public void setRecentActivity(List<BranchPerformanceDTO> recentActivity) {
        this.recentActivity = recentActivity;
    }

    public List<BranchPerformanceDTO> getBranchComparison() {
        return branchComparison;
    }

    public void setBranchComparison(List<BranchPerformanceDTO> branchComparison) {
        this.branchComparison = branchComparison;
    }

    public List<ChartDataDTO> getRevenueByBranch() {
        return revenueByBranch;
    }

    public void setRevenueByBranch(List<ChartDataDTO> revenueByBranch) {
        this.revenueByBranch = revenueByBranch;
    }

    public List<ChartDataDTO> getAppointmentsByBranch() {
        return appointmentsByBranch;
    }

    public void setAppointmentsByBranch(List<ChartDataDTO> appointmentsByBranch) {
        this.appointmentsByBranch = appointmentsByBranch;
    }

    public List<ChartDataDTO> getMonthlyTrend() {
        return monthlyTrend;
    }

    public void setMonthlyTrend(List<ChartDataDTO> monthlyTrend) {
        this.monthlyTrend = monthlyTrend;
    }

    public List<ChartDataDTO> getBranchShare() {
        return branchShare;
    }

    public void setBranchShare(List<ChartDataDTO> branchShare) {
        this.branchShare = branchShare;
    }
}
