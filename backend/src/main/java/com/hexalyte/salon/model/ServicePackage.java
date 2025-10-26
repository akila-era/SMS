package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_packages")
@EntityListeners(AuditingEntityListener.class)
public class ServicePackage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @Min(value = 0)
    @Column(name = "discount_percentage")
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "total_duration_minutes")
    private Integer totalDurationMinutes;

    @Column(name = "can_split_sessions")
    private Boolean canSplitSessions = false;

    @Column(name = "max_validity_days")
    private Integer maxValidityDays;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "servicePackage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServicePackageItem> packageItems = new ArrayList<>();

    // Constructors
    public ServicePackage() {}

    public ServicePackage(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getTotalDurationMinutes() {
        return totalDurationMinutes;
    }

    public void setTotalDurationMinutes(Integer totalDurationMinutes) {
        this.totalDurationMinutes = totalDurationMinutes;
    }

    public Boolean getCanSplitSessions() {
        return canSplitSessions;
    }

    public void setCanSplitSessions(Boolean canSplitSessions) {
        this.canSplitSessions = canSplitSessions;
    }

    public Integer getMaxValidityDays() {
        return maxValidityDays;
    }

    public void setMaxValidityDays(Integer maxValidityDays) {
        this.maxValidityDays = maxValidityDays;
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

    public List<ServicePackageItem> getPackageItems() {
        return packageItems;
    }

    public void setPackageItems(List<ServicePackageItem> packageItems) {
        this.packageItems = packageItems;
    }

    // Helper methods
    public BigDecimal getOriginalPrice() {
        if (discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            return price.divide(BigDecimal.ONE.subtract(discountPercentage.divide(new BigDecimal("100"))), 2, BigDecimal.ROUND_HALF_UP);
        }
        return price;
    }

    public BigDecimal getDiscountAmount() {
        return getOriginalPrice().subtract(price);
    }

    public void calculateTotalDuration() {
        if (packageItems != null && !packageItems.isEmpty()) {
            this.totalDurationMinutes = packageItems.stream()
                    .mapToInt(item -> item.getService().getTotalDurationMinutes())
                    .sum();
        }
    }
}
