package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "billing_item")
@EntityListeners(AuditingEntityListener.class)
public class BillingItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Billing billing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Column(name = "commission_generated")
    private Boolean commissionGenerated = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public BillingItem() {}

    public BillingItem(Billing billing, Service service, Staff staff, Integer quantity, BigDecimal unitPrice) {
        this.billing = billing;
        this.service = service;
        this.staff = staff;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotal();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotal();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Boolean getCommissionGenerated() {
        return commissionGenerated;
    }

    public void setCommissionGenerated(Boolean commissionGenerated) {
        this.commissionGenerated = commissionGenerated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    private void calculateTotal() {
        if (unitPrice != null && quantity != null) {
            this.total = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public BigDecimal calculateCommission() {
        if (service != null && total != null) {
            return service.getEffectiveCommission(total);
        }
        return BigDecimal.ZERO;
    }

    public void markCommissionGenerated() {
        this.commissionGenerated = true;
    }
}
