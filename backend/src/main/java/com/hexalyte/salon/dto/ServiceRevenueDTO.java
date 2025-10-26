package com.hexalyte.salon.dto;

import java.math.BigDecimal;

public class ServiceRevenueDTO {
    private Long serviceId;
    private String serviceName;
    private String serviceCategory;
    private BigDecimal totalRevenue;
    private Integer serviceCount;
    private BigDecimal averagePrice;
    private BigDecimal percentage;

    // Constructors
    public ServiceRevenueDTO() {}

    public ServiceRevenueDTO(Long serviceId, String serviceName, String serviceCategory, 
                           BigDecimal totalRevenue, Integer serviceCount) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceCategory = serviceCategory;
        this.totalRevenue = totalRevenue;
        this.serviceCount = serviceCount;
        this.averagePrice = serviceCount > 0 ? 
            totalRevenue.divide(BigDecimal.valueOf(serviceCount), 2, BigDecimal.ROUND_HALF_UP) : 
            BigDecimal.ZERO;
    }

    // Getters and Setters
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

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
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

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
