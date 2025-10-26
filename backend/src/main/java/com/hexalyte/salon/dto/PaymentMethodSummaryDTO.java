package com.hexalyte.salon.dto;

import java.math.BigDecimal;

public class PaymentMethodSummaryDTO {
    private String paymentMethod;
    private BigDecimal totalAmount;
    private Integer transactionCount;
    private BigDecimal percentage;

    // Constructors
    public PaymentMethodSummaryDTO() {}

    public PaymentMethodSummaryDTO(String paymentMethod, BigDecimal totalAmount, Integer transactionCount) {
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.transactionCount = transactionCount;
    }

    // Getters and Setters
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
