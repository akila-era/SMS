package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.BranchSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BranchSummaryRepository extends JpaRepository<BranchSummary, Long> {
    
    // Find summary by branch and date
    @Query("SELECT bs FROM BranchSummary bs WHERE bs.branch.id = :branchId AND bs.summaryDate = :summaryDate")
    Optional<BranchSummary> findByBranchIdAndSummaryDate(@Param("branchId") Long branchId, @Param("summaryDate") LocalDate summaryDate);
    
    // Find summaries for a branch within date range
    @Query("SELECT bs FROM BranchSummary bs WHERE bs.branch.id = :branchId AND bs.summaryDate BETWEEN :startDate AND :endDate ORDER BY bs.summaryDate DESC")
    List<BranchSummary> findByBranchIdAndSummaryDateBetweenOrderBySummaryDateDesc(
        @Param("branchId") Long branchId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find latest summary for a branch
    @Query("SELECT bs FROM BranchSummary bs WHERE bs.branch.id = :branchId ORDER BY bs.summaryDate DESC")
    List<BranchSummary> findLatestByBranchId(@Param("branchId") Long branchId);
    
    // Find summaries for all branches on a specific date
    List<BranchSummary> findBySummaryDateOrderByTotalRevenueDesc(LocalDate summaryDate);
    
    // Find top performing branches by revenue in date range
    @Query("SELECT bs FROM BranchSummary bs WHERE bs.summaryDate BETWEEN :startDate AND :endDate " +
           "ORDER BY bs.totalRevenue DESC")
    List<BranchSummary> findTopPerformingBranches(@Param("startDate") LocalDate startDate, 
                                                  @Param("endDate") LocalDate endDate);
    
    // Calculate total revenue for all branches in date range
    @Query("SELECT COALESCE(SUM(bs.totalRevenue), 0) FROM BranchSummary bs WHERE bs.summaryDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalRevenueInDateRange(@Param("startDate") LocalDate startDate, 
                                         @Param("endDate") LocalDate endDate);
    
    // Calculate total appointments for all branches in date range
    @Query("SELECT COALESCE(SUM(bs.totalAppointments), 0) FROM BranchSummary bs WHERE bs.summaryDate BETWEEN :startDate AND :endDate")
    Long getTotalAppointmentsInDateRange(@Param("startDate") LocalDate startDate, 
                                        @Param("endDate") LocalDate endDate);
    
    // Find branch performance comparison
    @Query("SELECT bs.branch.id, bs.branch.branchName, " +
           "SUM(bs.totalRevenue) as totalRevenue, " +
           "SUM(bs.totalAppointments) as totalAppointments, " +
           "AVG(bs.averageRating) as avgRating " +
           "FROM BranchSummary bs " +
           "WHERE bs.summaryDate BETWEEN :startDate AND :endDate " +
           "GROUP BY bs.branch.id, bs.branch.branchName " +
           "ORDER BY totalRevenue DESC")
    List<Object[]> getBranchPerformanceComparison(@Param("startDate") LocalDate startDate, 
                                                  @Param("endDate") LocalDate endDate);
    
    // Find inactive branches (no activity in date range)
    @Query("SELECT b FROM Branch b WHERE b.id NOT IN " +
           "(SELECT DISTINCT bs.branch.id FROM BranchSummary bs WHERE bs.summaryDate BETWEEN :startDate AND :endDate)")
    List<Object> findInactiveBranches(@Param("startDate") LocalDate startDate, 
                                      @Param("endDate") LocalDate endDate);
    
    // Get monthly revenue trend for a branch
    @Query("SELECT bs.summaryDate, bs.totalRevenue FROM BranchSummary bs " +
           "WHERE bs.branch.id = :branchId AND bs.summaryDate BETWEEN :startDate AND :endDate " +
           "ORDER BY bs.summaryDate ASC")
    List<Object[]> getMonthlyRevenueTrend(@Param("branchId") Long branchId,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);
}
