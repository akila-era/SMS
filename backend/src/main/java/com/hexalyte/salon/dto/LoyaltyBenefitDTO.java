package com.hexalyte.salon.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LoyaltyBenefitDTO {
    
    private Long id;
    private String membershipLevel;
    private String benefitType; // "DISCOUNT", "FREE_SERVICE", "PRIORITY_BOOKING", "BONUS_POINTS"
    private String description;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private String freeServiceId;
    private String freeServiceName;
    private Integer bonusPointsMultiplier;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public LoyaltyBenefitDTO() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMembershipLevel() {
        return membershipLevel;
    }
    
    public void setMembershipLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }
    
    public String getBenefitType() {
        return benefitType;
    }
    
    public void setBenefitType(String benefitType) {
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
    
    public String getFreeServiceId() {
        return freeServiceId;
    }
    
    public void setFreeServiceId(String freeServiceId) {
        this.freeServiceId = freeServiceId;
    }
    
    public String getFreeServiceName() {
        return freeServiceName;
    }
    
    public void setFreeServiceName(String freeServiceName) {
        this.freeServiceName = freeServiceName;
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
}
