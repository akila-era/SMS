package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    
    // Find by billing
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.billing.billId = :billId ORDER BY pt.createdAt ASC")
    List<PaymentTransaction> findByBillingIdOrderByCreatedAtAsc(@Param("billId") Long billId);
    
    // Find by payment method
    List<PaymentTransaction> findByPaymentMethodOrderByCreatedAtDesc(String paymentMethod);
    
    // Find by status
    List<PaymentTransaction> findByStatusOrderByCreatedAtDesc(PaymentTransaction.TransactionStatus status);
    
    // Find by reference number
    Optional<PaymentTransaction> findByReferenceNo(String referenceNo);
    
    // Find by gateway transaction ID
    Optional<PaymentTransaction> findByGatewayTransactionId(String gatewayTransactionId);
    
    // Find by billing and payment method
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.billing.billId = :billId AND pt.paymentMethod = :paymentMethod ORDER BY pt.createdAt ASC")
    List<PaymentTransaction> findByBillingIdAndPaymentMethodOrderByCreatedAtAsc(@Param("billId") Long billId, @Param("paymentMethod") String paymentMethod);
    
    // Find by billing and status
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.billing.billId = :billId AND pt.status = :status ORDER BY pt.createdAt ASC")
    List<PaymentTransaction> findByBillingIdAndStatusOrderByCreatedAtAsc(@Param("billId") Long billId, @Param("status") PaymentTransaction.TransactionStatus status);
    
    // Find by date range
    List<PaymentTransaction> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find by branch and date range
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.billing.branch.id = :branchId " +
           "AND pt.createdAt BETWEEN :startDate AND :endDate ORDER BY pt.createdAt DESC")
    List<PaymentTransaction> findByBranchIdAndDateRange(@Param("branchId") Long branchId, 
                                                       @Param("startDate") LocalDateTime startDate, 
                                                       @Param("endDate") LocalDateTime endDate);
    
    // Find by payment method and date range
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.paymentMethod = :paymentMethod " +
           "AND pt.createdAt BETWEEN :startDate AND :endDate ORDER BY pt.createdAt DESC")
    List<PaymentTransaction> findByPaymentMethodAndDateRange(@Param("paymentMethod") String paymentMethod, 
                                                            @Param("startDate") LocalDateTime startDate, 
                                                            @Param("endDate") LocalDateTime endDate);
    
    // Find by branch, payment method and date range
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.billing.branch.id = :branchId " +
           "AND pt.paymentMethod = :paymentMethod AND pt.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY pt.createdAt DESC")
    List<PaymentTransaction> findByBranchIdAndPaymentMethodAndDateRange(@Param("branchId") Long branchId, 
                                                                       @Param("paymentMethod") String paymentMethod, 
                                                                       @Param("startDate") LocalDateTime startDate, 
                                                                       @Param("endDate") LocalDateTime endDate);
    
    // Find successful transactions
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.status = 'SUCCESS' ORDER BY pt.createdAt DESC")
    List<PaymentTransaction> findSuccessfulTransactions();
    
    // Find successful transactions by branch
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.billing.branch.id = :branchId " +
           "AND pt.status = 'SUCCESS' ORDER BY pt.createdAt DESC")
    List<PaymentTransaction> findSuccessfulTransactionsByBranch(@Param("branchId") Long branchId);
    
    // Find failed transactions
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.status = 'FAILED' ORDER BY pt.createdAt DESC")
    List<PaymentTransaction> findFailedTransactions();
    
    // Find pending transactions
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.status = 'PENDING' ORDER BY pt.createdAt ASC")
    List<PaymentTransaction> findPendingTransactions();
    
    // Find refunded transactions
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.status = 'REFUNDED' ORDER BY pt.createdAt DESC")
    List<PaymentTransaction> findRefundedTransactions();
    
    // Revenue by payment method
    @Query("SELECT pt.paymentMethod, COALESCE(SUM(pt.amount), 0) as totalAmount, COUNT(pt) as transactionCount " +
           "FROM PaymentTransaction pt WHERE pt.billing.branch.id = :branchId AND pt.status = 'SUCCESS' " +
           "AND pt.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY pt.paymentMethod ORDER BY totalAmount DESC")
    List<Object[]> getPaymentMethodRevenueByBranchAndDateRange(@Param("branchId") Long branchId, 
                                                              @Param("startDate") LocalDateTime startDate, 
                                                              @Param("endDate") LocalDateTime endDate);
    
    // Total revenue by branch
    @Query("SELECT COALESCE(SUM(pt.amount), 0) FROM PaymentTransaction pt WHERE pt.billing.branch.id = :branchId AND pt.status = 'SUCCESS'")
    BigDecimal getTotalRevenueByBranch(@Param("branchId") Long branchId);
    
    // Total revenue by branch and date range
    @Query("SELECT COALESCE(SUM(pt.amount), 0) FROM PaymentTransaction pt WHERE pt.billing.branch.id = :branchId " +
           "AND pt.status = 'SUCCESS' AND pt.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal getTotalRevenueByBranchAndDateRange(@Param("branchId") Long branchId, 
                                                 @Param("startDate") LocalDateTime startDate, 
                                                 @Param("endDate") LocalDateTime endDate);
    
    // Transaction count by status
    @Query("SELECT pt.status, COUNT(pt) as transactionCount FROM PaymentTransaction pt " +
           "WHERE pt.billing.branch.id = :branchId AND pt.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY pt.status")
    List<Object[]> getTransactionCountByStatusAndBranchAndDateRange(@Param("branchId") Long branchId, 
                                                                   @Param("startDate") LocalDateTime startDate, 
                                                                   @Param("endDate") LocalDateTime endDate);
    
    // Find transactions by customer
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.billing.customer.id = :customerId " +
           "ORDER BY pt.createdAt DESC")
    List<PaymentTransaction> findByCustomerId(@Param("customerId") Long customerId);
    
    // Find transactions by customer and date range
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.billing.customer.id = :customerId " +
           "AND pt.createdAt BETWEEN :startDate AND :endDate ORDER BY pt.createdAt DESC")
    List<PaymentTransaction> findByCustomerIdAndDateRange(@Param("customerId") Long customerId, 
                                                         @Param("startDate") LocalDateTime startDate, 
                                                         @Param("endDate") LocalDateTime endDate);
    
    // Find transactions by staff
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.billing.appointment.staff.id = :staffId " +
           "ORDER BY pt.createdAt DESC")
    List<PaymentTransaction> findByStaffId(@Param("staffId") Long staffId);
    
    // Find transactions by staff and date range
    @Query("SELECT pt FROM PaymentTransaction pt WHERE pt.billing.appointment.staff.id = :staffId " +
           "AND pt.createdAt BETWEEN :startDate AND :endDate ORDER BY pt.createdAt DESC")
    List<PaymentTransaction> findByStaffIdAndDateRange(@Param("staffId") Long staffId, 
                                                      @Param("startDate") LocalDateTime startDate, 
                                                      @Param("endDate") LocalDateTime endDate);
    
    // Check if reference number exists
    boolean existsByReferenceNo(String referenceNo);
    
    // Check if gateway transaction ID exists
    boolean existsByGatewayTransactionId(String gatewayTransactionId);
}
