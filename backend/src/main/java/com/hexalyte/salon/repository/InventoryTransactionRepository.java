package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.InventoryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    
    @Query("SELECT it FROM InventoryTransaction it WHERE it.branch.id = :branchId")
    List<InventoryTransaction> findByBranchId(@Param("branchId") Long branchId);
    
    @Query("SELECT it FROM InventoryTransaction it WHERE it.product.id = :productId")
    List<InventoryTransaction> findByProductId(@Param("productId") Long productId);
    
    List<InventoryTransaction> findByType(InventoryTransaction.TransactionType type);
    
    @Query("SELECT it FROM InventoryTransaction it WHERE it.supplier.id = :supplierId")
    List<InventoryTransaction> findBySupplierId(@Param("supplierId") Long supplierId);
    
    @Query("SELECT it FROM InventoryTransaction it WHERE it.createdBy.id = :createdBy")
    List<InventoryTransaction> findByCreatedBy(@Param("createdBy") Long createdBy);
    
    @Query("SELECT it FROM InventoryTransaction it WHERE " +
           "(:branchId IS NULL OR it.branch.id = :branchId) AND " +
           "(:productId IS NULL OR it.product.id = :productId) AND " +
           "(:type IS NULL OR it.type = :type) AND " +
           "(:supplierId IS NULL OR it.supplier.id = :supplierId) AND " +
           "(:fromDate IS NULL OR it.createdAt >= :fromDate) AND " +
           "(:toDate IS NULL OR it.createdAt <= :toDate)")
    Page<InventoryTransaction> findByFilters(@Param("branchId") Long branchId,
                                           @Param("productId") Long productId,
                                           @Param("type") InventoryTransaction.TransactionType type,
                                           @Param("supplierId") Long supplierId,
                                           @Param("fromDate") LocalDateTime fromDate,
                                           @Param("toDate") LocalDateTime toDate,
                                           Pageable pageable);
    
    @Query("SELECT it FROM InventoryTransaction it WHERE it.branch.id = :branchId AND " +
           "it.createdAt >= :fromDate AND it.createdAt <= :toDate ORDER BY it.createdAt DESC")
    List<InventoryTransaction> findByBranchIdAndDateRange(@Param("branchId") Long branchId,
                                                         @Param("fromDate") LocalDateTime fromDate,
                                                         @Param("toDate") LocalDateTime toDate);
    
    @Query("SELECT SUM(it.totalAmount) FROM InventoryTransaction it WHERE it.branch.id = :branchId AND " +
           "it.type = 'PURCHASE' AND it.createdAt >= :fromDate AND it.createdAt <= :toDate")
    BigDecimal calculateTotalPurchaseAmount(@Param("branchId") Long branchId,
                                          @Param("fromDate") LocalDateTime fromDate,
                                          @Param("toDate") LocalDateTime toDate);
    
    @Query("SELECT COUNT(it) FROM InventoryTransaction it WHERE it.branch.id = :branchId AND " +
           "it.type = 'PURCHASE' AND it.createdAt >= :fromDate AND it.createdAt <= :toDate")
    Long countPurchasesByBranchAndDateRange(@Param("branchId") Long branchId,
                                           @Param("fromDate") LocalDateTime fromDate,
                                           @Param("toDate") LocalDateTime toDate);
    
    @Query("SELECT it FROM InventoryTransaction it WHERE it.branch.id = :branchId ORDER BY it.createdAt DESC")
    List<InventoryTransaction> findRecentTransactionsByBranch(@Param("branchId") Long branchId, Pageable pageable);
}
