package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.PromotionUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromotionUsageRepository extends JpaRepository<PromotionUsage, Long> {
    
    @Query("SELECT pu FROM PromotionUsage pu WHERE pu.promotion.id = :promotionId")
    List<PromotionUsage> findByPromotionId(@Param("promotionId") Long promotionId);
    
    @Query("SELECT pu FROM PromotionUsage pu WHERE pu.appointment.id = :appointmentId")
    List<PromotionUsage> findByAppointmentId(@Param("appointmentId") Long appointmentId);
    
    @Query("SELECT pu FROM PromotionUsage pu WHERE pu.promotion.id = :promotionId AND DATE(pu.createdAt) = :date")
    List<PromotionUsage> findByPromotionIdAndDate(@Param("promotionId") Long promotionId, @Param("date") LocalDate date);
    
    @Query("SELECT pu FROM PromotionUsage pu WHERE pu.promotion.id = :promotionId AND pu.createdAt >= :startDate AND pu.createdAt <= :endDate")
    List<PromotionUsage> findByPromotionIdAndDateRange(@Param("promotionId") Long promotionId, @Param("startDate") java.time.LocalDateTime startDate, @Param("endDate") java.time.LocalDateTime endDate);
    
    @Query("SELECT COUNT(pu) FROM PromotionUsage pu WHERE pu.promotion.id = :promotionId")
    Long countUsagesByPromotionId(@Param("promotionId") Long promotionId);
}
