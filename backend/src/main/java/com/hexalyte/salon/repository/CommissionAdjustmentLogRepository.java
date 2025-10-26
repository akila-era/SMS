package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.CommissionAdjustmentLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommissionAdjustmentLogRepository extends JpaRepository<CommissionAdjustmentLog, Long> {
    
    // Find adjustment logs by commission
    List<CommissionAdjustmentLog> findByCommissionId(Long commissionId);
    
    // Find adjustment logs by user who made the change
    List<CommissionAdjustmentLog> findByChangedBy(Long changedBy);
    
    // Find adjustment logs by adjustment type
    List<CommissionAdjustmentLog> findByAdjustmentType(CommissionAdjustmentLog.AdjustmentType adjustmentType);
    
    // Find adjustment logs by date range
    @Query("SELECT cal FROM CommissionAdjustmentLog cal WHERE cal.changedAt BETWEEN :startDate AND :endDate")
    List<CommissionAdjustmentLog> findByChangedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                                        @Param("endDate") LocalDateTime endDate);
    
    // Find adjustment logs by commission and date range
    @Query("SELECT cal FROM CommissionAdjustmentLog cal WHERE " +
           "cal.commission.id = :commissionId AND " +
           "cal.changedAt BETWEEN :startDate AND :endDate")
    List<CommissionAdjustmentLog> findByCommissionIdAndChangedAtBetween(@Param("commissionId") Long commissionId,
                                                                        @Param("startDate") LocalDateTime startDate,
                                                                        @Param("endDate") LocalDateTime endDate);
    
    // Find adjustment logs by user and date range
    @Query("SELECT cal FROM CommissionAdjustmentLog cal WHERE " +
           "cal.changedBy = :changedBy AND " +
           "cal.changedAt BETWEEN :startDate AND :endDate")
    List<CommissionAdjustmentLog> findByChangedByAndChangedAtBetween(@Param("changedBy") Long changedBy,
                                                                    @Param("startDate") LocalDateTime startDate,
                                                                    @Param("endDate") LocalDateTime endDate);
    
    // Find adjustment logs by multiple criteria
    @Query("SELECT cal FROM CommissionAdjustmentLog cal WHERE " +
           "(:commissionId IS NULL OR cal.commission.id = :commissionId) AND " +
           "(:changedBy IS NULL OR cal.changedBy = :changedBy) AND " +
           "(:adjustmentType IS NULL OR cal.adjustmentType = :adjustmentType) AND " +
           "(:startDate IS NULL OR cal.changedAt >= :startDate) AND " +
           "(:endDate IS NULL OR cal.changedAt <= :endDate)")
    Page<CommissionAdjustmentLog> findByMultipleCriteria(@Param("commissionId") Long commissionId,
                                                        @Param("changedBy") Long changedBy,
                                                        @Param("adjustmentType") CommissionAdjustmentLog.AdjustmentType adjustmentType,
                                                        @Param("startDate") LocalDateTime startDate,
                                                        @Param("endDate") LocalDateTime endDate,
                                                        Pageable pageable);
    
    // Find recent adjustment logs
    @Query("SELECT cal FROM CommissionAdjustmentLog cal ORDER BY cal.changedAt DESC")
    List<CommissionAdjustmentLog> findRecentAdjustments(Pageable pageable);
    
    // Find adjustment logs by commission ordered by date
    @Query("SELECT cal FROM CommissionAdjustmentLog cal WHERE cal.commission.id = :commissionId ORDER BY cal.changedAt DESC")
    List<CommissionAdjustmentLog> findByCommissionIdOrderByChangedAtDesc(@Param("commissionId") Long commissionId);
    
    // Count adjustments by type
    @Query("SELECT cal.adjustmentType, COUNT(cal) FROM CommissionAdjustmentLog cal " +
           "WHERE cal.changedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY cal.adjustmentType")
    List<Object[]> countAdjustmentsByType(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    // Find adjustments by reason containing text
    @Query("SELECT cal FROM CommissionAdjustmentLog cal WHERE cal.reason LIKE %:reason%")
    List<CommissionAdjustmentLog> findByReasonContaining(@Param("reason") String reason);
    
    // Find adjustments by IP address
    List<CommissionAdjustmentLog> findByIpAddress(String ipAddress);
    
    // Find adjustments by user agent containing text
    @Query("SELECT cal FROM CommissionAdjustmentLog cal WHERE cal.userAgent LIKE %:userAgent%")
    List<CommissionAdjustmentLog> findByUserAgentContaining(@Param("userAgent") String userAgent);
    
    // Find total adjustments made by a user
    @Query("SELECT COUNT(cal) FROM CommissionAdjustmentLog cal WHERE cal.changedBy = :changedBy")
    Long countByChangedBy(@Param("changedBy") Long changedBy);
    
    // Find adjustments for a specific commission in chronological order
    @Query("SELECT cal FROM CommissionAdjustmentLog cal WHERE cal.commission.id = :commissionId ORDER BY cal.changedAt ASC")
    List<CommissionAdjustmentLog> findCommissionAdjustmentHistory(@Param("commissionId") Long commissionId);
    
    // Find adjustments made today
    @Query("SELECT cal FROM CommissionAdjustmentLog cal WHERE DATE(cal.changedAt) = CURRENT_DATE ORDER BY cal.changedAt DESC")
    List<CommissionAdjustmentLog> findTodayAdjustments();
    
    // Find adjustments made this week
    @Query("SELECT cal FROM CommissionAdjustmentLog cal WHERE cal.changedAt >= :weekStart ORDER BY cal.changedAt DESC")
    List<CommissionAdjustmentLog> findThisWeekAdjustments(@Param("weekStart") LocalDateTime weekStart);
    
    // Find adjustments made this month
    @Query("SELECT cal FROM CommissionAdjustmentLog cal WHERE " +
           "YEAR(cal.changedAt) = :year AND MONTH(cal.changedAt) = :month " +
           "ORDER BY cal.changedAt DESC")
    List<CommissionAdjustmentLog> findThisMonthAdjustments(@Param("year") int year, @Param("month") int month);
    
    // Find most active users making adjustments
    @Query("SELECT cal.changedBy, COUNT(cal) as adjustmentCount " +
           "FROM CommissionAdjustmentLog cal " +
           "WHERE cal.changedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY cal.changedBy " +
           "ORDER BY adjustmentCount DESC")
    List<Object[]> findMostActiveAdjustmentUsers(@Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate, 
                                                Pageable pageable);
}
