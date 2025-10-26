package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.CommissionSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommissionSummaryRepository extends JpaRepository<CommissionSummary, Long> {
    
    // Find summary by staff and month
    @Query("SELECT cs FROM CommissionSummary cs WHERE cs.staff.id = :staffId AND cs.month = :month")
    Optional<CommissionSummary> findByStaffIdAndMonth(@Param("staffId") Long staffId, @Param("month") String month);
    
    // Find summary by branch and month
    @Query("SELECT cs FROM CommissionSummary cs WHERE cs.branch.id = :branchId AND cs.month = :month")
    List<CommissionSummary> findByBranchIdAndMonth(@Param("branchId") Long branchId, @Param("month") String month);
    
    // Find summary by staff, branch and month
    @Query("SELECT cs FROM CommissionSummary cs WHERE cs.staff.id = :staffId AND cs.branch.id = :branchId AND cs.month = :month")
    Optional<CommissionSummary> findByStaffIdAndBranchIdAndMonth(@Param("staffId") Long staffId, @Param("branchId") Long branchId, @Param("month") String month);
    
    // Find summaries by status
    List<CommissionSummary> findByStatus(CommissionSummary.SummaryStatus status);
    
    // Find summaries by staff
    @Query("SELECT cs FROM CommissionSummary cs WHERE cs.staff.id = :staffId")
    List<CommissionSummary> findByStaffId(@Param("staffId") Long staffId);
    
    // Find summaries by branch
    @Query("SELECT cs FROM CommissionSummary cs WHERE cs.branch.id = :branchId")
    List<CommissionSummary> findByBranchId(@Param("branchId") Long branchId);
    
    // Find summaries by month
    List<CommissionSummary> findByMonth(String month);
    
    // Find summaries by staff and status
    @Query("SELECT cs FROM CommissionSummary cs WHERE cs.staff.id = :staffId AND cs.status = :status")
    List<CommissionSummary> findByStaffIdAndStatus(@Param("staffId") Long staffId, @Param("status") CommissionSummary.SummaryStatus status);
    
    // Find summaries by branch and status
    @Query("SELECT cs FROM CommissionSummary cs WHERE cs.branch.id = :branchId AND cs.status = :status")
    List<CommissionSummary> findByBranchIdAndStatus(@Param("branchId") Long branchId, @Param("status") CommissionSummary.SummaryStatus status);
    
    // Find summaries by month and status
    List<CommissionSummary> findByMonthAndStatus(String month, CommissionSummary.SummaryStatus status);
    
    // Find pending summaries
    @Query("SELECT cs FROM CommissionSummary cs WHERE cs.status = 'PENDING' ORDER BY cs.month DESC, cs.staff.firstName ASC")
    List<CommissionSummary> findPendingSummaries();
    
    // Find approved summaries
    @Query("SELECT cs FROM CommissionSummary cs WHERE cs.status = 'APPROVED' ORDER BY cs.month DESC, cs.staff.firstName ASC")
    List<CommissionSummary> findApprovedSummaries();
    
    // Find locked summaries
    @Query("SELECT cs FROM CommissionSummary cs WHERE cs.status = 'LOCKED' ORDER BY cs.month DESC, cs.staff.firstName ASC")
    List<CommissionSummary> findLockedSummaries();
    
    // Find summaries by multiple criteria
    @Query("SELECT cs FROM CommissionSummary cs WHERE " +
           "(:staffId IS NULL OR cs.staff.id = :staffId) AND " +
           "(:branchId IS NULL OR cs.branch.id = :branchId) AND " +
           "(:status IS NULL OR cs.status = :status) AND " +
           "(:month IS NULL OR cs.month = :month)")
    Page<CommissionSummary> findByMultipleCriteria(@Param("staffId") Long staffId,
                                                  @Param("branchId") Long branchId,
                                                  @Param("status") CommissionSummary.SummaryStatus status,
                                                  @Param("month") String month,
                                                  Pageable pageable);
    
    // Calculate total commission for all staff in a month
    @Query("SELECT COALESCE(SUM(cs.totalCommission), 0) FROM CommissionSummary cs WHERE " +
           "cs.month = :month AND cs.status != 'PENDING'")
    BigDecimal calculateTotalCommissionForMonth(@Param("month") String month);
    
    // Calculate total commission for branch in a month
    @Query("SELECT COALESCE(SUM(cs.totalCommission), 0) FROM CommissionSummary cs WHERE " +
           "cs.branch.id = :branchId AND cs.month = :month AND cs.status != 'PENDING'")
    BigDecimal calculateTotalCommissionForBranchInMonth(@Param("branchId") Long branchId, @Param("month") String month);
    
    // Calculate total commission for staff in a month
    @Query("SELECT COALESCE(SUM(cs.totalCommission), 0) FROM CommissionSummary cs WHERE " +
           "cs.staff.id = :staffId AND cs.month = :month AND cs.status != 'PENDING'")
    BigDecimal calculateTotalCommissionForStaffInMonth(@Param("staffId") Long staffId, @Param("month") String month);
    
    // Find top earning staff in a month
    @Query("SELECT cs.staff.id, cs.staff.firstName, cs.staff.lastName, cs.branch.branchName, " +
           "cs.totalCommission, cs.totalServices " +
           "FROM CommissionSummary cs WHERE " +
           "cs.month = :month AND cs.status != 'PENDING' " +
           "ORDER BY cs.totalCommission DESC")
    List<Object[]> findTopEarningStaffInMonth(@Param("month") String month, Pageable pageable);
    
    // Find branch-wise commission summary for a month
    @Query("SELECT cs.branch.id, cs.branch.branchName, " +
           "SUM(cs.totalCommission) as totalCommission, SUM(cs.totalServices) as totalServices " +
           "FROM CommissionSummary cs WHERE " +
           "cs.month = :month AND cs.status != 'PENDING' " +
           "GROUP BY cs.branch.id, cs.branch.branchName " +
           "ORDER BY totalCommission DESC")
    List<Object[]> findBranchWiseCommissionInMonth(@Param("month") String month);
    
    // Find summaries by year
    @Query("SELECT cs FROM CommissionSummary cs WHERE cs.month LIKE :year% ORDER BY cs.month DESC, cs.staff.firstName ASC")
    List<CommissionSummary> findByYear(@Param("year") String year);
    
    // Find summaries by quarter - simplified approach
    @Query("SELECT cs FROM CommissionSummary cs WHERE " +
           "cs.month LIKE :year% " +
           "ORDER BY cs.month DESC, cs.staff.firstName ASC")
    List<CommissionSummary> findByQuarter(@Param("year") String year);
    
    // Count pending summaries
    @Query("SELECT COUNT(cs) FROM CommissionSummary cs WHERE cs.status = 'PENDING'")
    Long countPendingSummaries();
    
    // Calculate total pending commission amount
    @Query("SELECT COALESCE(SUM(cs.totalCommission), 0) FROM CommissionSummary cs WHERE cs.status = 'PENDING'")
    BigDecimal calculateTotalPendingCommission();
    
    // Find summaries that need to be generated for a month
    @Query("SELECT DISTINCT c.staff.id, c.branch.id FROM Commission c WHERE " +
           "YEAR(c.calculatedOn) = :year AND MONTH(c.calculatedOn) = :month AND c.status != 'REVERSED' " +
           "AND NOT EXISTS (SELECT cs FROM CommissionSummary cs WHERE " +
           "cs.staff.id = c.staff.id AND cs.branch.id = c.branch.id AND cs.month = :monthString)")
    List<Object[]> findStaffBranchCombinationsForMonth(@Param("year") int year, 
                                                      @Param("month") int month, 
                                                      @Param("monthString") String monthString);
    
    // Check if summary exists for staff, branch and month
    @Query("SELECT COUNT(cs) > 0 FROM CommissionSummary cs WHERE " +
           "cs.staff.id = :staffId AND cs.branch.id = :branchId AND cs.month = :month")
    boolean existsByStaffIdAndBranchIdAndMonth(@Param("staffId") Long staffId, 
                                              @Param("branchId") Long branchId, 
                                              @Param("month") String month);
}
