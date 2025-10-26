package com.hexalyte.salon.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class PaymentRequestDTO {
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
    private BigDecimal amount;
    
    private String referenceNo;
    private String gatewayTransactionId;
    private String gatewayResponse;

    // Constructors
    public PaymentRequestDTO() {}

    public PaymentRequestDTO(String paymentMethod, BigDecimal amount) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    public PaymentRequestDTO(String paymentMethod, BigDecimal amount, String referenceNo) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.referenceNo = referenceNo;
    }

    // Getters and Setters
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

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }

    public String getGatewayResponse() {
        return gatewayResponse;
    }

    public void setGatewayResponse(String gatewayResponse) {
        this.gatewayResponse = gatewayResponse;
    }
}
