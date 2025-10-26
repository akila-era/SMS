package com.hexalyte.salon.dto;

import com.hexalyte.salon.model.PaymentTransaction;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentTransactionDTO {
    private Long transactionId;
    private Long billId;
    private String billNumber;
    private String paymentMethod;
    private BigDecimal amount;
    private String referenceNo;
    private String status;
    private String gatewayResponse;
    private String gatewayTransactionId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // Constructors
    public PaymentTransactionDTO() {}

    public PaymentTransactionDTO(PaymentTransaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.billId = transaction.getBilling().getBillId();
        this.billNumber = transaction.getBilling().getBillNumber();
        this.paymentMethod = transaction.getPaymentMethod();
        this.amount = transaction.getAmount();
        this.referenceNo = transaction.getReferenceNo();
        this.status = transaction.getStatus().name();
        this.gatewayResponse = transaction.getGatewayResponse();
        this.gatewayTransactionId = transaction.getGatewayTransactionId();
        this.createdAt = transaction.getCreatedAt();
    }

    // Getters and Setters
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGatewayResponse() {
        return gatewayResponse;
    }

    public void setGatewayResponse(String gatewayResponse) {
        this.gatewayResponse = gatewayResponse;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public boolean isSuccessful() {
        return "SUCCESS".equals(status);
    }

    public boolean isFailed() {
        return "FAILED".equals(status);
    }

    public boolean isPending() {
        return "PENDING".equals(status);
    }

    public boolean isRefunded() {
        return "REFUNDED".equals(status);
    }
}
