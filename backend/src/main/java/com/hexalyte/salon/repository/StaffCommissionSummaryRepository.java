package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.StaffCommissionSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffCommissionSummaryRepository extends JpaRepository<StaffCommissionSummary, Long> {
    
    @Query("SELECT s FROM StaffCommissionSummary s WHERE s.staff.id = :staffId")
    List<StaffCommissionSummary> findByStaffId(@Param("staffId") Long staffId);
    
    @Query("SELECT s FROM StaffCommissionSummary s WHERE s.branch.id = :branchId")
    List<StaffCommissionSummary> findByBranchId(@Param("branchId") Long branchId);
    
    List<StaffCommissionSummary> findByMonth(String month);
    
    @Query("SELECT s FROM StaffCommissionSummary s WHERE s.staff.id = :staffId AND s.month = :month")
    Optional<StaffCommissionSummary> findByStaffIdAndMonth(@Param("staffId") Long staffId, @Param("month") String month);
    
    @Query("SELECT s FROM StaffCommissionSummary s WHERE s.branch.id = :branchId AND s.month = :month")
    List<StaffCommissionSummary> findByBranchIdAndMonth(@Param("branchId") Long branchId, @Param("month") String month);
    
    @Query("SELECT s FROM StaffCommissionSummary s WHERE s.staff.id = :staffId AND s.month BETWEEN :startMonth AND :endMonth ORDER BY s.month DESC")
    List<StaffCommissionSummary> findByStaffIdAndMonthRange(@Param("staffId") Long staffId, 
                                                           @Param("startMonth") String startMonth, 
                                                           @Param("endMonth") String endMonth);
    
    @Query("SELECT s FROM StaffCommissionSummary s WHERE s.branch.id = :branchId AND s.month BETWEEN :startMonth AND :endMonth ORDER BY s.month DESC")
    List<StaffCommissionSummary> findByBranchIdAndMonthRange(@Param("branchId") Long branchId, 
                                                            @Param("startMonth") String startMonth, 
                                                            @Param("endMonth") String endMonth);
    
    @Query("SELECT s FROM StaffCommissionSummary s WHERE s.month = :month ORDER BY s.totalPayout DESC")
    List<StaffCommissionSummary> findByMonthOrderByTotalPayoutDesc(@Param("month") String month);
    
    @Query("SELECT s FROM StaffCommissionSummary s WHERE s.month = :month ORDER BY s.totalServices DESC")
    List<StaffCommissionSummary> findByMonthOrderByTotalServicesDesc(@Param("month") String month);
    
    @Query("SELECT s FROM StaffCommissionSummary s WHERE s.month = :month ORDER BY s.averageRating DESC")
    List<StaffCommissionSummary> findByMonthOrderByAverageRatingDesc(@Param("month") String month);
    
    @Query("SELECT SUM(s.totalPayout) FROM StaffCommissionSummary s WHERE s.branch.id = :branchId AND s.month = :month")
    Double sumTotalPayoutByBranchAndMonth(@Param("branchId") Long branchId, @Param("month") String month);
    
    @Query("SELECT SUM(s.totalCommission) FROM StaffCommissionSummary s WHERE s.branch.id = :branchId AND s.month = :month")
    Double sumTotalCommissionByBranchAndMonth(@Param("branchId") Long branchId, @Param("month") String month);
    
    @Query("SELECT COUNT(s) FROM StaffCommissionSummary s WHERE s.branch.id = :branchId AND s.month = :month")
    Long countByBranchAndMonth(@Param("branchId") Long branchId, @Param("month") String month);
    
    @Query("SELECT COUNT(s) > 0 FROM StaffCommissionSummary s WHERE s.staff.id = :staffId AND s.month = :month")
    boolean existsByStaffIdAndMonth(@Param("staffId") Long staffId, @Param("month") String month);
}
