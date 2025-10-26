package com.hexalyte.salon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BillingReportDTO {
    private Long branchId;
    private String branchName;
    private LocalDate reportDate;
    private LocalDateTime generatedAt;
    
    // Summary totals
    private BigDecimal totalRevenue;
    private BigDecimal totalTax;
    private BigDecimal totalDiscount;
    private BigDecimal totalLoyaltyRedeemed;
    private Integer totalBills;
    private Integer paidBills;
    private Integer partialBills;
    private Integer unpaidBills;
    
    // Payment method breakdown
    private List<PaymentMethodSummaryDTO> paymentMethodBreakdown;
    
    // Daily trends
    private List<DailyRevenueDTO> dailyRevenue;
    
    // Top services
    private List<ServiceRevenueDTO> topServices;
    
    // Staff performance
    private List<StaffRevenueDTO> staffPerformance;

    // Constructors
    public BillingReportDTO() {}

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

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getTotalLoyaltyRedeemed() {
        return totalLoyaltyRedeemed;
    }

    public void setTotalLoyaltyRedeemed(BigDecimal totalLoyaltyRedeemed) {
        this.totalLoyaltyRedeemed = totalLoyaltyRedeemed;
    }

    public Integer getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(Integer totalBills) {
        this.totalBills = totalBills;
    }

    public Integer getPaidBills() {
        return paidBills;
    }

    public void setPaidBills(Integer paidBills) {
        this.paidBills = paidBills;
    }

    public Integer getPartialBills() {
        return partialBills;
    }

    public void setPartialBills(Integer partialBills) {
        this.partialBills = partialBills;
    }

    public Integer getUnpaidBills() {
        return unpaidBills;
    }

    public void setUnpaidBills(Integer unpaidBills) {
        this.unpaidBills = unpaidBills;
    }

    public List<PaymentMethodSummaryDTO> getPaymentMethodBreakdown() {
        return paymentMethodBreakdown;
    }

    public void setPaymentMethodBreakdown(List<PaymentMethodSummaryDTO> paymentMethodBreakdown) {
        this.paymentMethodBreakdown = paymentMethodBreakdown;
    }

    public List<DailyRevenueDTO> getDailyRevenue() {
        return dailyRevenue;
    }

    public void setDailyRevenue(List<DailyRevenueDTO> dailyRevenue) {
        this.dailyRevenue = dailyRevenue;
    }

    public List<ServiceRevenueDTO> getTopServices() {
        return topServices;
    }

    public void setTopServices(List<ServiceRevenueDTO> topServices) {
        this.topServices = topServices;
    }

    public List<StaffRevenueDTO> getStaffPerformance() {
        return staffPerformance;
    }

    public void setStaffPerformance(List<StaffRevenueDTO> staffPerformance) {
        this.staffPerformance = staffPerformance;
    }
}
