package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Billing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    
    // Find by bill number
    Optional<Billing> findByBillNumber(String billNumber);
    
    // Find by appointment
    Optional<Billing> findByAppointmentId(Long appointmentId);
    
    // Find by customer
    @Query("SELECT b FROM Billing b WHERE b.customer.id = :customerId ORDER BY b.billDate DESC")
    List<Billing> findByCustomerIdOrderByBillDateDesc(@Param("customerId") Long customerId);
    
    // Find by branch
    @Query("SELECT b FROM Billing b WHERE b.branch.id = :branchId ORDER BY b.billDate DESC")
    List<Billing> findByBranchIdOrderByBillDateDesc(@Param("branchId") Long branchId);
    
    // Find by branch with pagination
    @Query("SELECT b FROM Billing b WHERE b.branch.id = :branchId ORDER BY b.billDate DESC")
    Page<Billing> findByBranchIdOrderByBillDateDesc(@Param("branchId") Long branchId, Pageable pageable);
    
    // Find by status
    List<Billing> findByStatusOrderByBillDateDesc(Billing.BillingStatus status);
    
    // Find by branch and status
    @Query("SELECT b FROM Billing b WHERE b.branch.id = :branchId AND b.status = :status ORDER BY b.billDate DESC")
    List<Billing> findByBranchIdAndStatusOrderByBillDateDesc(@Param("branchId") Long branchId, @Param("status") Billing.BillingStatus status);
    
    // Find by date range
    List<Billing> findByBillDateBetweenOrderByBillDateDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find by branch and date range
    @Query("SELECT b FROM Billing b WHERE b.branch.id = :branchId AND b.billDate BETWEEN :startDate AND :endDate ORDER BY b.billDate DESC")
    List<Billing> findByBranchIdAndBillDateBetweenOrderByBillDateDesc(@Param("branchId") Long branchId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find by customer and date range
    @Query("SELECT b FROM Billing b WHERE b.customer.id = :customerId AND b.billDate BETWEEN :startDate AND :endDate ORDER BY b.billDate DESC")
    List<Billing> findByCustomerIdAndBillDateBetweenOrderByBillDateDesc(@Param("customerId") Long customerId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find unpaid bills
    @Query("SELECT b FROM Billing b WHERE b.status IN ('UNPAID', 'PARTIAL') ORDER BY b.billDate DESC")
    List<Billing> findUnpaidBills();
    
    // Find unpaid bills by branch
    @Query("SELECT b FROM Billing b WHERE b.branch.id = :branchId AND b.status IN ('UNPAID', 'PARTIAL') ORDER BY b.billDate DESC")
    List<Billing> findUnpaidBillsByBranch(@Param("branchId") Long branchId);
    
    // Find bills by payment method
    List<Billing> findByPaymentMethodOrderByBillDateDesc(String paymentMethod);
    
    // Find bills by payment method and branch
    @Query("SELECT b FROM Billing b WHERE b.branch.id = :branchId AND b.paymentMethod = :paymentMethod ORDER BY b.billDate DESC")
    List<Billing> findByBranchIdAndPaymentMethodOrderByBillDateDesc(@Param("branchId") Long branchId, @Param("paymentMethod") String paymentMethod);
    
    // Revenue queries
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Billing b WHERE b.branch.id = :branchId AND b.status = 'PAID'")
    BigDecimal getTotalRevenueByBranch(@Param("branchId") Long branchId);
    
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Billing b WHERE b.branch.id = :branchId AND b.status = 'PAID' AND b.billDate BETWEEN :startDate AND :endDate")
    BigDecimal getRevenueByBranchAndDateRange(@Param("branchId") Long branchId, 
                                            @Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COALESCE(SUM(b.taxAmount), 0) FROM Billing b WHERE b.branch.id = :branchId AND b.status = 'PAID' AND b.billDate BETWEEN :startDate AND :endDate")
    BigDecimal getTaxCollectedByBranchAndDateRange(@Param("branchId") Long branchId, 
                                                 @Param("startDate") LocalDateTime startDate, 
                                                 @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COALESCE(SUM(b.discountAmount), 0) FROM Billing b WHERE b.branch.id = :branchId AND b.status = 'PAID' AND b.billDate BETWEEN :startDate AND :endDate")
    BigDecimal getDiscountGivenByBranchAndDateRange(@Param("branchId") Long branchId, 
                                                  @Param("startDate") LocalDateTime startDate, 
                                                  @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COALESCE(SUM(b.loyaltyRedeemed), 0) FROM Billing b WHERE b.branch.id = :branchId AND b.status = 'PAID' AND b.billDate BETWEEN :startDate AND :endDate")
    BigDecimal getLoyaltyRedeemedByBranchAndDateRange(@Param("branchId") Long branchId, 
                                                    @Param("startDate") LocalDateTime startDate, 
                                                    @Param("endDate") LocalDateTime endDate);
    
    // Count queries
    @Query("SELECT COUNT(b) FROM Billing b WHERE b.branch.id = :branchId AND b.status = :status")
    Long countByBranchAndStatus(@Param("branchId") Long branchId, @Param("status") Billing.BillingStatus status);
    
    @Query("SELECT COUNT(b) FROM Billing b WHERE b.branch.id = :branchId AND b.billDate BETWEEN :startDate AND :endDate")
    Long countByBranchAndDateRange(@Param("branchId") Long branchId, 
                                  @Param("startDate") LocalDateTime startDate, 
                                  @Param("endDate") LocalDateTime endDate);
    
    // Daily revenue query
    @Query("SELECT DATE(b.billDate) as date, COALESCE(SUM(b.totalAmount), 0) as revenue, COUNT(b) as billCount " +
           "FROM Billing b WHERE b.branch.id = :branchId AND b.status = 'PAID' AND b.billDate BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(b.billDate) ORDER BY DATE(b.billDate)")
    List<Object[]> getDailyRevenueByBranchAndDateRange(@Param("branchId") Long branchId, 
                                                      @Param("startDate") LocalDateTime startDate, 
                                                      @Param("endDate") LocalDateTime endDate);
    
    // Payment method breakdown
    @Query("SELECT b.paymentMethod, COALESCE(SUM(b.totalAmount), 0) as totalAmount, COUNT(b) as transactionCount " +
           "FROM Billing b WHERE b.branch.id = :branchId AND b.status = 'PAID' AND b.billDate BETWEEN :startDate AND :endDate " +
           "GROUP BY b.paymentMethod ORDER BY totalAmount DESC")
    List<Object[]> getPaymentMethodBreakdownByBranchAndDateRange(@Param("branchId") Long branchId, 
                                                               @Param("startDate") LocalDateTime startDate, 
                                                               @Param("endDate") LocalDateTime endDate);
    
    // Check if appointment already has a bill
    boolean existsByAppointmentId(Long appointmentId);
    
    // Find bills by created by user
    @Query("SELECT b FROM Billing b WHERE b.createdBy.id = :createdById ORDER BY b.billDate DESC")
    List<Billing> findByCreatedByIdOrderByBillDateDesc(@Param("createdById") Long createdById);
    
    // Find bills by created by user and branch
    @Query("SELECT b FROM Billing b WHERE b.createdBy.id = :createdById AND b.branch.id = :branchId ORDER BY b.billDate DESC")
    List<Billing> findByCreatedByIdAndBranchIdOrderByBillDateDesc(@Param("createdById") Long createdById, @Param("branchId") Long branchId);
    
    // Count by branch and bill number prefix
    Long countByBranchIdAndBillNumberStartingWith(Long branchId, String prefix);
}
