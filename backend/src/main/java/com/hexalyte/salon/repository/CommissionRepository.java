package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Commission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long> {
    
    // Find commissions by staff
    @Query("SELECT c FROM Commission c WHERE c.staff.id = :staffId")
    List<Commission> findByStaffId(@Param("staffId") Long staffId);
    
    // Find commissions by branch
    @Query("SELECT c FROM Commission c WHERE c.branch.id = :branchId")
    List<Commission> findByBranchId(@Param("branchId") Long branchId);
    
    // Find commissions by appointment
    @Query("SELECT c FROM Commission c WHERE c.appointment.id = :appointmentId")
    List<Commission> findByAppointmentId(@Param("appointmentId") Long appointmentId);
    
    // Find commissions by service
    @Query("SELECT c FROM Commission c WHERE c.service.id = :serviceId")
    List<Commission> findByServiceId(@Param("serviceId") Long serviceId);
    
    // Find commissions by status
    List<Commission> findByStatus(Commission.CommissionStatus status);
    
    // Find commissions by staff and status
    @Query("SELECT c FROM Commission c WHERE c.staff.id = :staffId AND c.status = :status")
    List<Commission> findByStaffIdAndStatus(@Param("staffId") Long staffId, @Param("status") Commission.CommissionStatus status);
    
    // Find commissions by branch and status
    @Query("SELECT c FROM Commission c WHERE c.branch.id = :branchId AND c.status = :status")
    List<Commission> findByBranchIdAndStatus(@Param("branchId") Long branchId, @Param("status") Commission.CommissionStatus status);
    
    // Find commissions by date range
    @Query("SELECT c FROM Commission c WHERE c.calculatedOn BETWEEN :startDate AND :endDate")
    List<Commission> findByCalculatedOnBetween(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
    
    // Find commissions by staff and date range
    @Query("SELECT c FROM Commission c WHERE c.staff.id = :staffId AND c.calculatedOn BETWEEN :startDate AND :endDate")
    List<Commission> findByStaffIdAndCalculatedOnBetween(@Param("staffId") Long staffId,
                                                        @Param("startDate") LocalDateTime startDate,
                                                        @Param("endDate") LocalDateTime endDate);
    
    // Find commissions by branch and date range
    @Query("SELECT c FROM Commission c WHERE c.branch.id = :branchId AND c.calculatedOn BETWEEN :startDate AND :endDate")
    List<Commission> findByBranchIdAndCalculatedOnBetween(@Param("branchId") Long branchId,
                                                         @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);
    
    // Find commissions by multiple criteria
    @Query("SELECT c FROM Commission c WHERE " +
           "(:staffId IS NULL OR c.staff.id = :staffId) AND " +
           "(:branchId IS NULL OR c.branch.id = :branchId) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:startDate IS NULL OR c.calculatedOn >= :startDate) AND " +
           "(:endDate IS NULL OR c.calculatedOn <= :endDate)")
    Page<Commission> findByMultipleCriteria(@Param("staffId") Long staffId,
                                           @Param("branchId") Long branchId,
                                           @Param("status") Commission.CommissionStatus status,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           Pageable pageable);
    
    // Calculate total commission amount for staff in a month
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM Commission c WHERE " +
           "c.staff.id = :staffId AND " +
           "c.branch.id = :branchId AND " +
           "YEAR(c.calculatedOn) = :year AND " +
           "MONTH(c.calculatedOn) = :month AND " +
           "c.status != 'REVERSED'")
    BigDecimal calculateTotalCommissionForStaffInMonth(@Param("staffId") Long staffId,
                                                      @Param("branchId") Long branchId,
                                                      @Param("year") int year,
                                                      @Param("month") int month);
    
    // Count total services for staff in a month
    @Query("SELECT COUNT(c) FROM Commission c WHERE " +
           "c.staff.id = :staffId AND " +
           "c.branch.id = :branchId AND " +
           "YEAR(c.calculatedOn) = :year AND " +
           "MONTH(c.calculatedOn) = :month AND " +
           "c.status != 'REVERSED'")
    Long countServicesForStaffInMonth(@Param("staffId") Long staffId,
                                     @Param("branchId") Long branchId,
                                     @Param("year") int year,
                                     @Param("month") int month);
    
    // Calculate total commission amount for branch in a month
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM Commission c WHERE " +
           "c.branch.id = :branchId AND " +
           "YEAR(c.calculatedOn) = :year AND " +
           "MONTH(c.calculatedOn) = :month AND " +
           "c.status != 'REVERSED'")
    BigDecimal calculateTotalCommissionForBranchInMonth(@Param("branchId") Long branchId,
                                                       @Param("year") int year,
                                                       @Param("month") int month);
    
    // Count total services for branch in a month
    @Query("SELECT COUNT(c) FROM Commission c WHERE " +
           "c.branch.id = :branchId AND " +
           "YEAR(c.calculatedOn) = :year AND " +
           "MONTH(c.calculatedOn) = :month AND " +
           "c.status != 'REVERSED'")
    Long countServicesForBranchInMonth(@Param("branchId") Long branchId,
                                      @Param("year") int year,
                                      @Param("month") int month);
    
    // Find top earning staff in a month
    @Query("SELECT c.staff.id, c.staff.firstName, c.staff.lastName, c.branch.branchName, " +
           "SUM(c.amount) as totalEarnings, COUNT(c) as totalServices " +
           "FROM Commission c WHERE " +
           "YEAR(c.calculatedOn) = :year AND " +
           "MONTH(c.calculatedOn) = :month AND " +
           "c.status != 'REVERSED' " +
           "GROUP BY c.staff.id, c.staff.firstName, c.staff.lastName, c.branch.branchName " +
           "ORDER BY totalEarnings DESC")
    List<Object[]> findTopEarningStaffInMonth(@Param("year") int year, @Param("month") int month, Pageable pageable);
    
    // Find commissions pending approval
    @Query("SELECT c FROM Commission c WHERE c.status = 'PENDING' ORDER BY c.calculatedOn ASC")
    List<Commission> findPendingApprovals();
    
    // Find commissions by appointment and service
    @Query("SELECT c FROM Commission c WHERE c.appointment.id = :appointmentId AND c.service.id = :serviceId")
    Optional<Commission> findByAppointmentIdAndServiceId(@Param("appointmentId") Long appointmentId,
                                                        @Param("serviceId") Long serviceId);
    
    // Find manual commissions
    List<Commission> findByIsManualTrue();
    
    // Find commissions by calculation rule
    @Query("SELECT c FROM Commission c WHERE c.calculationRule LIKE %:rule%")
    List<Commission> findByCalculationRuleContaining(@Param("rule") String rule);
    
    // Calculate total pending commission amount
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM Commission c WHERE c.status = 'PENDING'")
    BigDecimal calculateTotalPendingCommission();
    
    // Calculate total approved commission amount for a month
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM Commission c WHERE " +
           "c.status = 'APPROVED' AND " +
           "YEAR(c.calculatedOn) = :year AND " +
           "MONTH(c.calculatedOn) = :month")
    BigDecimal calculateTotalApprovedCommissionInMonth(@Param("year") int year, @Param("month") int month);
    
    // Find commissions by appointment, service, and staff
    @Query("SELECT c FROM Commission c WHERE c.appointment.id = :appointmentId AND c.service.id = :serviceId AND c.staff.id = :staffId")
    List<Commission> findByAppointmentIdAndServiceIdAndStaffId(@Param("appointmentId") Long appointmentId, @Param("serviceId") Long serviceId, @Param("staffId") Long staffId);
    
    // Find staff-branch combinations for a specific month
    @Query("SELECT DISTINCT c.staff.id, c.branch.id FROM Commission c WHERE " +
           "YEAR(c.calculatedOn) = :year AND " +
           "MONTH(c.calculatedOn) = :month AND " +
           "c.status != 'REVERSED'")
    List<Object[]> findStaffBranchCombinationsForMonth(@Param("year") int year, @Param("month") int month);
}