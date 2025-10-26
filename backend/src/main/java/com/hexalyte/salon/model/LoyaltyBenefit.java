package com.hexalyte.salon.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loyalty_benefits")
@EntityListeners(AuditingEntityListener.class)
public class LoyaltyBenefit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "membership_level", nullable = false)
    private Customer.MembershipLevel membershipLevel;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "benefit_type", nullable = false)
    private BenefitType benefitType;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "discount_percentage")
    private BigDecimal discountPercentage;
    
    @Column(name = "discount_amount")
    private BigDecimal discountAmount;
    
    @Column(name = "free_service_id")
    private Long freeServiceId;
    
    @Column(name = "bonus_points_multiplier")
    private Integer bonusPointsMultiplier;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public LoyaltyBenefit() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Customer.MembershipLevel getMembershipLevel() {
        return membershipLevel;
    }
    
    public void setMembershipLevel(Customer.MembershipLevel membershipLevel) {
        this.membershipLevel = membershipLevel;
    }
    
    public BenefitType getBenefitType() {
        return benefitType;
    }
    
    public void setBenefitType(BenefitType benefitType) {
        this.benefitType = benefitType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public Long getFreeServiceId() {
        return freeServiceId;
    }
    
    public void setFreeServiceId(Long freeServiceId) {
        this.freeServiceId = freeServiceId;
    }
    
    public Integer getBonusPointsMultiplier() {
        return bonusPointsMultiplier;
    }
    
    public void setBonusPointsMultiplier(Integer bonusPointsMultiplier) {
        this.bonusPointsMultiplier = bonusPointsMultiplier;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
    
    public enum BenefitType {
        DISCOUNT, FREE_SERVICE, PRIORITY_BOOKING, BONUS_POINTS
    }
}
