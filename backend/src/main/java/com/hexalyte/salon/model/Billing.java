package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "billing")
@EntityListeners(AuditingEntityListener.class)
public class Billing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "bill_number", unique = true, nullable = false)
    private String billNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull
    @Column(name = "bill_date", nullable = false)
    private LocalDateTime billDate;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "discount_amount")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "tax_amount")
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "loyalty_redeemed")
    private BigDecimal loyaltyRedeemed = BigDecimal.ZERO;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "paid_amount")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "balance_amount")
    private BigDecimal balanceAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BillingStatus status = BillingStatus.UNPAID;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "billing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BillingItem> billingItems = new ArrayList<>();

    @OneToMany(mappedBy = "billing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentTransaction> paymentTransactions = new ArrayList<>();

    // Enums
    public enum BillingStatus {
        PAID, PARTIAL, UNPAID, REFUNDED, CANCELLED
    }

    // Constructors
    public Billing() {}

    public Billing(String billNumber, Branch branch, Appointment appointment, Customer customer, 
                   BigDecimal subtotal, BigDecimal totalAmount) {
        this.billNumber = billNumber;
        this.branch = branch;
        this.appointment = appointment;
        this.customer = customer;
        this.subtotal = subtotal;
        this.totalAmount = totalAmount;
        this.billDate = LocalDateTime.now();
        this.balanceAmount = totalAmount;
    }

    // Getters and Setters
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

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getLoyaltyRedeemed() {
        return loyaltyRedeemed;
    }

    public void setLoyaltyRedeemed(BigDecimal loyaltyRedeemed) {
        this.loyaltyRedeemed = loyaltyRedeemed;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public BillingStatus getStatus() {
        return status;
    }

    public void setStatus(BillingStatus status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
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

    public List<BillingItem> getBillingItems() {
        return billingItems;
    }

    public void setBillingItems(List<BillingItem> billingItems) {
        this.billingItems = billingItems;
    }

    public List<PaymentTransaction> getPaymentTransactions() {
        return paymentTransactions;
    }

    public void setPaymentTransactions(List<PaymentTransaction> paymentTransactions) {
        this.paymentTransactions = paymentTransactions;
    }

    // Helper methods
    public boolean isPaid() {
        return status == BillingStatus.PAID;
    }

    public boolean isPartiallyPaid() {
        return status == BillingStatus.PARTIAL;
    }

    public boolean isUnpaid() {
        return status == BillingStatus.UNPAID;
    }

    public boolean isRefunded() {
        return status == BillingStatus.REFUNDED;
    }

    public void updatePaymentStatus() {
        if (paidAmount.compareTo(BigDecimal.ZERO) == 0) {
            this.status = BillingStatus.UNPAID;
        } else if (paidAmount.compareTo(totalAmount) >= 0) {
            this.status = BillingStatus.PAID;
        } else {
            this.status = BillingStatus.PARTIAL;
        }
        this.balanceAmount = totalAmount.subtract(paidAmount);
    }

    public void addPayment(BigDecimal amount) {
        this.paidAmount = this.paidAmount.add(amount);
        updatePaymentStatus();
    }

    public BigDecimal getEffectiveDiscount() {
        return discountAmount.add(loyaltyRedeemed);
    }
}
