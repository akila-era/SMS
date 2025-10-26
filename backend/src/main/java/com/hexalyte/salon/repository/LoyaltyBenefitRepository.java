package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Customer;
import com.hexalyte.salon.model.LoyaltyBenefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoyaltyBenefitRepository extends JpaRepository<LoyaltyBenefit, Long> {
    
    List<LoyaltyBenefit> findByMembershipLevelAndIsActiveTrue(Customer.MembershipLevel membershipLevel);
    
    List<LoyaltyBenefit> findByBenefitTypeAndIsActiveTrue(LoyaltyBenefit.BenefitType benefitType);
    
    @Query("SELECT lb FROM LoyaltyBenefit lb WHERE lb.isActive = true ORDER BY lb.membershipLevel, lb.benefitType")
    List<LoyaltyBenefit> findAllActiveOrderedByLevelAndType();
    
    @Query("SELECT lb FROM LoyaltyBenefit lb WHERE lb.membershipLevel = :level AND lb.benefitType = :type AND lb.isActive = true")
    List<LoyaltyBenefit> findByLevelAndType(Customer.MembershipLevel level, LoyaltyBenefit.BenefitType type);
}
