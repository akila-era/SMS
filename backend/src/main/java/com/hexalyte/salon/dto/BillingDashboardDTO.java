package com.hexalyte.salon.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BillingDashboardDTO {
    private Long branchId;
    private String branchName;
    private LocalDate reportDate;
    
    // Today's summary
    private BigDecimal todayRevenue;
    private Integer todayBills;
    private BigDecimal todayTax;
    private BigDecimal todayDiscount;
    
    // This month's summary
    private BigDecimal monthRevenue;
    private Integer monthBills;
    private BigDecimal monthTax;
    private BigDecimal monthDiscount;
    
    // Payment status counts
    private Integer paidBills;
    private Integer partialBills;
    private Integer unpaidBills;
    
    // Recent bills
    private List<BillingDTO> recentBills;
    
    // Payment method breakdown for today
    private List<PaymentMethodSummaryDTO> todayPaymentBreakdown;
    
    // Top services today
    private List<ServiceRevenueDTO> topServicesToday;
    
    // Staff performance today
    private List<StaffRevenueDTO> staffPerformanceToday;

    // Constructors
    public BillingDashboardDTO() {}

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

    public BigDecimal getTodayRevenue() {
        return todayRevenue;
    }

    public void setTodayRevenue(BigDecimal todayRevenue) {
        this.todayRevenue = todayRevenue;
    }

    public Integer getTodayBills() {
        return todayBills;
    }

    public void setTodayBills(Integer todayBills) {
        this.todayBills = todayBills;
    }

    public BigDecimal getTodayTax() {
        return todayTax;
    }

    public void setTodayTax(BigDecimal todayTax) {
        this.todayTax = todayTax;
    }

    public BigDecimal getTodayDiscount() {
        return todayDiscount;
    }

    public void setTodayDiscount(BigDecimal todayDiscount) {
        this.todayDiscount = todayDiscount;
    }

    public BigDecimal getMonthRevenue() {
        return monthRevenue;
    }

    public void setMonthRevenue(BigDecimal monthRevenue) {
        this.monthRevenue = monthRevenue;
    }

    public Integer getMonthBills() {
        return monthBills;
    }

    public void setMonthBills(Integer monthBills) {
        this.monthBills = monthBills;
    }

    public BigDecimal getMonthTax() {
        return monthTax;
    }

    public void setMonthTax(BigDecimal monthTax) {
        this.monthTax = monthTax;
    }

    public BigDecimal getMonthDiscount() {
        return monthDiscount;
    }

    public void setMonthDiscount(BigDecimal monthDiscount) {
        this.monthDiscount = monthDiscount;
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

    public List<BillingDTO> getRecentBills() {
        return recentBills;
    }

    public void setRecentBills(List<BillingDTO> recentBills) {
        this.recentBills = recentBills;
    }

    public List<PaymentMethodSummaryDTO> getTodayPaymentBreakdown() {
        return todayPaymentBreakdown;
    }

    public void setTodayPaymentBreakdown(List<PaymentMethodSummaryDTO> todayPaymentBreakdown) {
        this.todayPaymentBreakdown = todayPaymentBreakdown;
    }

    public List<ServiceRevenueDTO> getTopServicesToday() {
        return topServicesToday;
    }

    public void setTopServicesToday(List<ServiceRevenueDTO> topServicesToday) {
        this.topServicesToday = topServicesToday;
    }

    public List<StaffRevenueDTO> getStaffPerformanceToday() {
        return staffPerformanceToday;
    }

    public void setStaffPerformanceToday(List<StaffRevenueDTO> staffPerformanceToday) {
        this.staffPerformanceToday = staffPerformanceToday;
    }
}
