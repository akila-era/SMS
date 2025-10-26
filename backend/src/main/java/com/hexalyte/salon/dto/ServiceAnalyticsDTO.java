package com.hexalyte.salon.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ServiceAnalyticsDTO {
    
    private Long id;
    private Long serviceId;
    private String serviceName;
    private Long branchId;
    private String branchName;
    private LocalDate analyticsDate;
    private Integer totalBookings = 0;
    private BigDecimal totalRevenue = BigDecimal.ZERO;
    private BigDecimal totalCommission = BigDecimal.ZERO;
    private BigDecimal totalProductCost = BigDecimal.ZERO;
    private BigDecimal totalProfit = BigDecimal.ZERO;
    private Integer averageDurationMinutes = 0;
    private BigDecimal averageRating = BigDecimal.ZERO;
    private Integer cancellationCount = 0;
    private Integer noShowCount = 0;
    private String createdAt;
    private String updatedAt;
    private BigDecimal profitMargin;
    private BigDecimal averageRevenuePerBooking;

    // Constructors
    public ServiceAnalyticsDTO() {}

    public ServiceAnalyticsDTO(Long serviceId, LocalDate analyticsDate) {
        this.serviceId = serviceId;
        this.analyticsDate = analyticsDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public LocalDate getAnalyticsDate() {
        return analyticsDate;
    }

    public void setAnalyticsDate(LocalDate analyticsDate) {
        this.analyticsDate = analyticsDate;
    }

    public Integer getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(Integer totalBookings) {
        this.totalBookings = totalBookings;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(BigDecimal totalCommission) {
        this.totalCommission = totalCommission;
    }

    public BigDecimal getTotalProductCost() {
        return totalProductCost;
    }

    public void setTotalProductCost(BigDecimal totalProductCost) {
        this.totalProductCost = totalProductCost;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Integer getAverageDurationMinutes() {
        return averageDurationMinutes;
    }

    public void setAverageDurationMinutes(Integer averageDurationMinutes) {
        this.averageDurationMinutes = averageDurationMinutes;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getCancellationCount() {
        return cancellationCount;
    }

    public void setCancellationCount(Integer cancellationCount) {
        this.cancellationCount = cancellationCount;
    }

    public Integer getNoShowCount() {
        return noShowCount;
    }

    public void setNoShowCount(Integer noShowCount) {
        this.noShowCount = noShowCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(BigDecimal profitMargin) {
        this.profitMargin = profitMargin;
    }

    public BigDecimal getAverageRevenuePerBooking() {
        return averageRevenuePerBooking;
    }

    public void setAverageRevenuePerBooking(BigDecimal averageRevenuePerBooking) {
        this.averageRevenuePerBooking = averageRevenuePerBooking;
    }
}
