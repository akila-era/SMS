package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.ServicePromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServicePromotionRepository extends JpaRepository<ServicePromotion, Long> {
    
    List<ServicePromotion> findByIsActiveTrue();
    
    @Query("SELECT sp FROM ServicePromotion sp WHERE sp.service.id = :serviceId")
    List<ServicePromotion> findByServiceId(@Param("serviceId") Long serviceId);
    
    @Query("SELECT sp FROM ServicePromotion sp WHERE sp.servicePackage.id = :servicePackageId")
    List<ServicePromotion> findByServicePackageId(@Param("servicePackageId") Long servicePackageId);
    
    @Query("SELECT sp FROM ServicePromotion sp WHERE sp.isActive = true AND sp.startDate <= :currentDate AND sp.endDate >= :currentDate")
    List<ServicePromotion> findCurrentlyActivePromotions(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT sp FROM ServicePromotion sp WHERE sp.service.id = :serviceId AND sp.isActive = true AND sp.startDate <= :currentDate AND sp.endDate >= :currentDate")
    List<ServicePromotion> findActivePromotionsForService(@Param("serviceId") Long serviceId, @Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT sp FROM ServicePromotion sp WHERE sp.servicePackage.id = :packageId AND sp.isActive = true AND sp.startDate <= :currentDate AND sp.endDate >= :currentDate")
    List<ServicePromotion> findActivePromotionsForPackage(@Param("packageId") Long packageId, @Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT sp FROM ServicePromotion sp WHERE sp.isActive = true AND sp.startDate <= :currentDate AND sp.endDate >= :currentDate AND (sp.maxUses IS NULL OR sp.usedCount < sp.maxUses)")
    List<ServicePromotion> findAvailablePromotions(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT sp FROM ServicePromotion sp WHERE sp.isActive = true AND sp.startDate <= :currentDate AND sp.endDate >= :currentDate AND (sp.applicableBranches IS NULL OR sp.applicableBranches LIKE %:branchId%)")
    List<ServicePromotion> findPromotionsForBranch(@Param("currentDate") LocalDate currentDate, @Param("branchId") String branchId);
}
