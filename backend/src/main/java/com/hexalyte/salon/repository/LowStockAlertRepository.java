package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.LowStockAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LowStockAlertRepository extends JpaRepository<LowStockAlert, Long> {
    
    @Query("SELECT lsa FROM LowStockAlert lsa WHERE lsa.branch.id = :branchId")
    List<LowStockAlert> findByBranchId(@Param("branchId") Long branchId);
    
    @Query("SELECT lsa FROM LowStockAlert lsa WHERE lsa.product.id = :productId")
    List<LowStockAlert> findByProductId(@Param("productId") Long productId);
    
    List<LowStockAlert> findByAlertType(LowStockAlert.AlertType alertType);
    
    List<LowStockAlert> findByIsResolved(Boolean isResolved);
    
    @Query("SELECT lsa FROM LowStockAlert lsa WHERE " +
           "(:branchId IS NULL OR lsa.branch.id = :branchId) AND " +
           "(:productId IS NULL OR lsa.product.id = :productId) AND " +
           "(:alertType IS NULL OR lsa.alertType = :alertType) AND " +
           "(:isResolved IS NULL OR lsa.isResolved = :isResolved)")
    Page<LowStockAlert> findByFilters(@Param("branchId") Long branchId,
                                    @Param("productId") Long productId,
                                    @Param("alertType") LowStockAlert.AlertType alertType,
                                    @Param("isResolved") Boolean isResolved,
                                    Pageable pageable);
    
    @Query("SELECT lsa FROM LowStockAlert lsa WHERE lsa.branch.id = :branchId AND lsa.isResolved = false ORDER BY lsa.createdAt DESC")
    List<LowStockAlert> findUnresolvedAlertsByBranch(@Param("branchId") Long branchId);
    
    @Query("SELECT lsa FROM LowStockAlert lsa WHERE lsa.isResolved = false ORDER BY lsa.createdAt DESC")
    List<LowStockAlert> findUnresolvedAlerts();
    
    @Query("SELECT COUNT(lsa) FROM LowStockAlert lsa WHERE lsa.branch.id = :branchId AND lsa.isResolved = false")
    Long countUnresolvedAlertsByBranch(@Param("branchId") Long branchId);
    
    @Query("SELECT COUNT(lsa) FROM LowStockAlert lsa WHERE lsa.isResolved = false")
    Long countUnresolvedAlerts();
    
    @Query("SELECT COUNT(lsa) FROM LowStockAlert lsa WHERE lsa.branch.id = :branchId AND lsa.alertType = 'LOW_STOCK' AND lsa.isResolved = false")
    Long countLowStockAlertsByBranch(@Param("branchId") Long branchId);
    
    @Query("SELECT COUNT(lsa) FROM LowStockAlert lsa WHERE lsa.branch.id = :branchId AND lsa.alertType = 'OUT_OF_STOCK' AND lsa.isResolved = false")
    Long countOutOfStockAlertsByBranch(@Param("branchId") Long branchId);
    
    @Query("SELECT lsa FROM LowStockAlert lsa WHERE lsa.branch.id = :branchId AND lsa.product.id = :productId AND lsa.isResolved = false")
    Optional<LowStockAlert> findUnresolvedAlertByBranchAndProduct(@Param("branchId") Long branchId, @Param("productId") Long productId);
}
