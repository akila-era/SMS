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
@Table(name = "services")
@EntityListeners(AuditingEntityListener.class)
public class Service {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @Min(value = 1)
    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "commission_rate")
    private BigDecimal commissionRate = BigDecimal.ZERO;

    private String category;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Min(value = 0)
    @Column(name = "preparation_buffer_minutes")
    private Integer preparationBufferMinutes = 0;

    @Min(value = 0)
    @Column(name = "cleanup_buffer_minutes")
    private Integer cleanupBufferMinutes = 0;

    @Column(name = "default_products", columnDefinition = "TEXT")
    private String defaultProducts;

    @Column(name = "is_taxable")
    private Boolean isTaxable = true;

    @Column(name = "required_skills", columnDefinition = "TEXT")
    private String requiredSkills;

    @Column(name = "resource_requirements", columnDefinition = "TEXT")
    private String resourceRequirements;

    @Column(name = "commission_type")
    @Enumerated(EnumType.STRING)
    private CommissionType commissionType = CommissionType.PERCENTAGE;

    @Column(name = "fixed_commission_amount")
    private BigDecimal fixedCommissionAmount = BigDecimal.ZERO;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AppointmentService> appointmentServices = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceBranchConfiguration> branchConfigurations = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServicePackageItem> packageItems = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServicePromotion> promotions = new ArrayList<>();

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceProductUsage> productUsages = new ArrayList<>();

    // Enums
    public enum CommissionType {
        PERCENTAGE, FIXED_AMOUNT
    }

    // Constructors
    public Service() {}

    public Service(String name, String description, BigDecimal price, Integer durationMinutes, BigDecimal commissionRate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.commissionRate = commissionRate;
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

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getPreparationBufferMinutes() {
        return preparationBufferMinutes;
    }

    public void setPreparationBufferMinutes(Integer preparationBufferMinutes) {
        this.preparationBufferMinutes = preparationBufferMinutes;
    }

    public Integer getCleanupBufferMinutes() {
        return cleanupBufferMinutes;
    }

    public void setCleanupBufferMinutes(Integer cleanupBufferMinutes) {
        this.cleanupBufferMinutes = cleanupBufferMinutes;
    }

    public String getDefaultProducts() {
        return defaultProducts;
    }

    public void setDefaultProducts(String defaultProducts) {
        this.defaultProducts = defaultProducts;
    }

    public Boolean getIsTaxable() {
        return isTaxable;
    }

    public void setIsTaxable(Boolean isTaxable) {
        this.isTaxable = isTaxable;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getResourceRequirements() {
        return resourceRequirements;
    }

    public void setResourceRequirements(String resourceRequirements) {
        this.resourceRequirements = resourceRequirements;
    }

    public CommissionType getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(CommissionType commissionType) {
        this.commissionType = commissionType;
    }

    public BigDecimal getFixedCommissionAmount() {
        return fixedCommissionAmount;
    }

    public void setFixedCommissionAmount(BigDecimal fixedCommissionAmount) {
        this.fixedCommissionAmount = fixedCommissionAmount;
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

    public List<AppointmentService> getAppointmentServices() {
        return appointmentServices;
    }

    public void setAppointmentServices(List<AppointmentService> appointmentServices) {
        this.appointmentServices = appointmentServices;
    }

    public List<ServiceBranchConfiguration> getBranchConfigurations() {
        return branchConfigurations;
    }

    public void setBranchConfigurations(List<ServiceBranchConfiguration> branchConfigurations) {
        this.branchConfigurations = branchConfigurations;
    }

    public List<ServicePackageItem> getPackageItems() {
        return packageItems;
    }

    public void setPackageItems(List<ServicePackageItem> packageItems) {
        this.packageItems = packageItems;
    }

    public List<ServicePromotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<ServicePromotion> promotions) {
        this.promotions = promotions;
    }

    public List<ServiceProductUsage> getProductUsages() {
        return productUsages;
    }

    public void setProductUsages(List<ServiceProductUsage> productUsages) {
        this.productUsages = productUsages;
    }

    // Helper methods
    public Integer getTotalDurationMinutes() {
        return durationMinutes + (preparationBufferMinutes != null ? preparationBufferMinutes : 0) + 
               (cleanupBufferMinutes != null ? cleanupBufferMinutes : 0);
    }

    public BigDecimal getEffectiveCommission(BigDecimal servicePrice) {
        if (commissionType == CommissionType.FIXED_AMOUNT) {
            return fixedCommissionAmount != null ? fixedCommissionAmount : BigDecimal.ZERO;
        } else {
            return servicePrice.multiply(commissionRate != null ? commissionRate : BigDecimal.ZERO);
        }
    }
}


