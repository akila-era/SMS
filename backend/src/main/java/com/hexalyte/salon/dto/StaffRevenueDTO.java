package com.hexalyte.salon.dto;

import java.math.BigDecimal;

public class StaffRevenueDTO {
    private Long staffId;
    private String staffName;
    private String employeeCode;
    private String designation;
    private BigDecimal totalRevenue;
    private Integer serviceCount;
    private BigDecimal averageServiceValue;
    private BigDecimal totalCommission;
    private BigDecimal percentage;

    // Constructors
    public StaffRevenueDTO() {}

    public StaffRevenueDTO(Long staffId, String staffName, String employeeCode, String designation,
                          BigDecimal totalRevenue, Integer serviceCount, BigDecimal totalCommission) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.employeeCode = employeeCode;
        this.designation = designation;
        this.totalRevenue = totalRevenue;
        this.serviceCount = serviceCount;
        this.totalCommission = totalCommission;
        this.averageServiceValue = serviceCount > 0 ? 
            totalRevenue.divide(BigDecimal.valueOf(serviceCount), 2, BigDecimal.ROUND_HALF_UP) : 
            BigDecimal.ZERO;
    }

    // Getters and Setters
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

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(Integer serviceCount) {
        this.serviceCount = serviceCount;
    }

    public BigDecimal getAverageServiceValue() {
        return averageServiceValue;
    }

    public void setAverageServiceValue(BigDecimal averageServiceValue) {
        this.averageServiceValue = averageServiceValue;
    }

    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(BigDecimal totalCommission) {
        this.totalCommission = totalCommission;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
