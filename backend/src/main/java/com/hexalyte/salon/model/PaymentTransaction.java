package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transaction")
@EntityListeners(AuditingEntityListener.class)
public class PaymentTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Billing billing;

    @NotNull
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "reference_no")
    private String referenceNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status = TransactionStatus.SUCCESS;

    @Column(name = "gateway_response", columnDefinition = "TEXT")
    private String gatewayResponse;

    @Column(name = "gateway_transaction_id")
    private String gatewayTransactionId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Enums
    public enum TransactionStatus {
        SUCCESS, FAILED, PENDING, REFUNDED, CANCELLED
    }

    public enum PaymentMethod {
        CASH("Cash"),
        CARD("Credit/Debit Card"),
        ONLINE("Online Payment"),
        UPI("UPI"),
        QR_CODE("QR Code"),
        GIFT_CARD("Gift Card"),
        LOYALTY_POINTS("Loyalty Points"),
        BANK_TRANSFER("Bank Transfer");

        private final String displayName;

        PaymentMethod(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructors
    public PaymentTransaction() {}

    public PaymentTransaction(Billing billing, String paymentMethod, BigDecimal amount, String referenceNo) {
        this.billing = billing;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.referenceNo = referenceNo;
    }

    // Getters and Setters
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
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

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
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
        return status == TransactionStatus.SUCCESS;
    }

    public boolean isFailed() {
        return status == TransactionStatus.FAILED;
    }

    public boolean isPending() {
        return status == TransactionStatus.PENDING;
    }

    public boolean isRefunded() {
        return status == TransactionStatus.REFUNDED;
    }

    public void markAsSuccessful() {
        this.status = TransactionStatus.SUCCESS;
    }

    public void markAsFailed() {
        this.status = TransactionStatus.FAILED;
    }

    public void markAsRefunded() {
        this.status = TransactionStatus.REFUNDED;
    }
}
