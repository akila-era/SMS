package com.hexalyte.salon.dto;

import com.hexalyte.salon.model.Billing;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BillingDTO {
    private Long billId;
    private String billNumber;
    private Long branchId;
    private String branchName;
    private Long appointmentId;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime billDate;
    
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal loyaltyRedeemed;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal balanceAmount;
    private String status;
    private String paymentMethod;
    private String remarks;
    private String createdByName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private List<BillingItemDTO> billingItems;
    private List<PaymentTransactionDTO> paymentTransactions;

    // Constructors
    public BillingDTO() {}

    public BillingDTO(Billing billing) {
        this.billId = billing.getBillId();
        this.billNumber = billing.getBillNumber();
        this.branchId = billing.getBranch().getId();
        this.branchName = billing.getBranch().getBranchName();
        this.appointmentId = billing.getAppointment().getId();
        this.customerId = billing.getCustomer().getId();
        this.customerName = billing.getCustomer().getFullName();
        this.customerPhone = billing.getCustomer().getPhone();
        this.customerEmail = billing.getCustomer().getEmail();
        this.billDate = billing.getBillDate();
        this.subtotal = billing.getSubtotal();
        this.discountAmount = billing.getDiscountAmount();
        this.taxAmount = billing.getTaxAmount();
        this.loyaltyRedeemed = billing.getLoyaltyRedeemed();
        this.totalAmount = billing.getTotalAmount();
        this.paidAmount = billing.getPaidAmount();
        this.balanceAmount = billing.getBalanceAmount();
        this.status = billing.getStatus().name();
        this.paymentMethod = billing.getPaymentMethod();
        this.remarks = billing.getRemarks();
        this.createdByName = billing.getCreatedBy() != null ? 
            billing.getCreatedBy().getUsername() : null;
        this.createdAt = billing.getCreatedAt();
        this.updatedAt = billing.getUpdatedAt();
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

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
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

    public List<BillingItemDTO> getBillingItems() {
        return billingItems;
    }

    public void setBillingItems(List<BillingItemDTO> billingItems) {
        this.billingItems = billingItems;
    }

    public List<PaymentTransactionDTO> getPaymentTransactions() {
        return paymentTransactions;
    }

    public void setPaymentTransactions(List<PaymentTransactionDTO> paymentTransactions) {
        this.paymentTransactions = paymentTransactions;
    }

    // Helper methods
    public BigDecimal getEffectiveDiscount() {
        return (discountAmount != null ? discountAmount : BigDecimal.ZERO)
                .add(loyaltyRedeemed != null ? loyaltyRedeemed : BigDecimal.ZERO);
    }

    public boolean isPaid() {
        return "PAID".equals(status);
    }

    public boolean isPartiallyPaid() {
        return "PARTIAL".equals(status);
    }

    public boolean isUnpaid() {
        return "UNPAID".equals(status);
    }
}
