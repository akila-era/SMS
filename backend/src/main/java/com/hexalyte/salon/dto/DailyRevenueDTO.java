package com.hexalyte.salon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DailyRevenueDTO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private BigDecimal revenue;
    private Integer billCount;
    private BigDecimal averageBillValue;

    // Constructors
    public DailyRevenueDTO() {}

    public DailyRevenueDTO(LocalDate date, BigDecimal revenue, Integer billCount) {
        this.date = date;
        this.revenue = revenue;
        this.billCount = billCount;
        this.averageBillValue = billCount > 0 ? revenue.divide(BigDecimal.valueOf(billCount), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public Integer getBillCount() {
        return billCount;
    }

    public void setBillCount(Integer billCount) {
        this.billCount = billCount;
    }

    public BigDecimal getAverageBillValue() {
        return averageBillValue;
    }

    public void setAverageBillValue(BigDecimal averageBillValue) {
        this.averageBillValue = averageBillValue;
    }
}
