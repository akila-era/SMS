package com.hexalyte.salon.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_analytics")
@EntityListeners(AuditingEntityListener.class)
public class ServiceAnalytics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "analytics_date")
    private LocalDate analyticsDate;

    @Column(name = "total_bookings")
    private Integer totalBookings = 0;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "total_commission")
    private BigDecimal totalCommission = BigDecimal.ZERO;

    @Column(name = "total_product_cost")
    private BigDecimal totalProductCost = BigDecimal.ZERO;

    @Column(name = "total_profit")
    private BigDecimal totalProfit = BigDecimal.ZERO;

    @Column(name = "average_duration_minutes")
    private Integer averageDurationMinutes = 0;

    @Column(name = "average_rating")
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "cancellation_count")
    private Integer cancellationCount = 0;

    @Column(name = "no_show_count")
    private Integer noShowCount = 0;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public ServiceAnalytics() {}

    public ServiceAnalytics(Service service, LocalDate analyticsDate) {
        this.service = service;
        this.analyticsDate = analyticsDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    public void calculateProfit() {
        this.totalProfit = totalRevenue.subtract(totalCommission).subtract(totalProductCost);
    }

    public BigDecimal getProfitMargin() {
        if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            return totalProfit.divide(totalRevenue, 4, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getAverageRevenuePerBooking() {
        if (totalBookings > 0) {
            return totalRevenue.divide(new BigDecimal(totalBookings), 2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
