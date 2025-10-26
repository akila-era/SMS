package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "low_stock_alert")
public class LowStockAlert {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @NotNull(message = "Branch ID is required")
    @Column(name = "branch_id", nullable = false)
    private Long branchId;
    
    @NotNull(message = "Product ID is required")
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @NotNull(message = "Current quantity is required")
    @DecimalMin(value = "0.0", message = "Current quantity must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Current quantity must have at most 8 integer digits and 2 decimal places")
    @Column(name = "current_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentQuantity;
    
    @NotNull(message = "Alert quantity is required")
    @DecimalMin(value = "0.0", message = "Alert quantity must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Alert quantity must have at most 8 integer digits and 2 decimal places")
    @Column(name = "alert_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal alertQuantity;
    
    @NotNull(message = "Alert type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    private AlertType alertType;
    
    @Column(name = "is_resolved")
    private Boolean isResolved = false;
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    @Column(name = "resolved_by")
    private Long resolvedBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private Branch branch;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by", insertable = false, updatable = false)
    private User resolvedByUser;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Constructors
    public LowStockAlert() {}
    
    public LowStockAlert(Long branchId, Long productId, BigDecimal currentQuantity, 
                        BigDecimal alertQuantity, AlertType alertType) {
        this.branchId = branchId;
        this.productId = productId;
        this.currentQuantity = currentQuantity;
        this.alertQuantity = alertQuantity;
        this.alertType = alertType;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getBranchId() {
        return branchId;
    }
    
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public BigDecimal getCurrentQuantity() {
        return currentQuantity;
    }
    
    public void setCurrentQuantity(BigDecimal currentQuantity) {
        this.currentQuantity = currentQuantity;
    }
    
    public BigDecimal getAlertQuantity() {
        return alertQuantity;
    }
    
    public void setAlertQuantity(BigDecimal alertQuantity) {
        this.alertQuantity = alertQuantity;
    }
    
    public AlertType getAlertType() {
        return alertType;
    }
    
    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }
    
    public Boolean getIsResolved() {
        return isResolved;
    }
    
    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }
    
    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }
    
    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
    
    public Long getResolvedBy() {
        return resolvedBy;
    }
    
    public void setResolvedBy(Long resolvedBy) {
        this.resolvedBy = resolvedBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Branch getBranch() {
        return branch;
    }
    
    public void setBranch(Branch branch) {
        this.branch = branch;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public User getResolvedByUser() {
        return resolvedByUser;
    }
    
    public void setResolvedByUser(User resolvedByUser) {
        this.resolvedByUser = resolvedByUser;
    }
    
    // Enums
    public enum AlertType {
        LOW_STOCK, OUT_OF_STOCK
    }
}
