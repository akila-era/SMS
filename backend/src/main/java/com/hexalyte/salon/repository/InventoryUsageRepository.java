package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.InventoryUsage;
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
public interface InventoryUsageRepository extends JpaRepository<InventoryUsage, Long> {
    
    @Query("SELECT iu FROM InventoryUsage iu WHERE iu.appointment.id = :appointmentId")
    List<InventoryUsage> findByAppointmentId(@Param("appointmentId") Long appointmentId);
    
    @Query("SELECT iu FROM InventoryUsage iu WHERE iu.service.id = :serviceId")
    List<InventoryUsage> findByServiceId(@Param("serviceId") Long serviceId);
    
    @Query("SELECT iu FROM InventoryUsage iu WHERE iu.staff.id = :staffId")
    List<InventoryUsage> findByStaffId(@Param("staffId") Long staffId);
    
    @Query("SELECT iu FROM InventoryUsage iu WHERE iu.branch.id = :branchId")
    List<InventoryUsage> findByBranchId(@Param("branchId") Long branchId);
    
    @Query("SELECT iu FROM InventoryUsage iu WHERE iu.product.id = :productId")
    List<InventoryUsage> findByProductId(@Param("productId") Long productId);
    
    @Query("SELECT iu FROM InventoryUsage iu WHERE " +
           "(:branchId IS NULL OR iu.branch.id = :branchId) AND " +
           "(:productId IS NULL OR iu.product.id = :productId) AND " +
           "(:serviceId IS NULL OR iu.service.id = :serviceId) AND " +
           "(:staffId IS NULL OR iu.staff.id = :staffId) AND " +
           "(:fromDate IS NULL OR iu.usedAt >= :fromDate) AND " +
           "(:toDate IS NULL OR iu.usedAt <= :toDate)")
    Page<InventoryUsage> findByFilters(@Param("branchId") Long branchId,
                                     @Param("productId") Long productId,
                                     @Param("serviceId") Long serviceId,
                                     @Param("staffId") Long staffId,
                                     @Param("fromDate") LocalDateTime fromDate,
                                     @Param("toDate") LocalDateTime toDate,
                                     Pageable pageable);
    
    @Query("SELECT iu FROM InventoryUsage iu WHERE iu.branch.id = :branchId AND " +
           "iu.usedAt >= :fromDate AND iu.usedAt <= :toDate ORDER BY iu.usedAt DESC")
    List<InventoryUsage> findByBranchIdAndDateRange(@Param("branchId") Long branchId,
                                                   @Param("fromDate") LocalDateTime fromDate,
                                                   @Param("toDate") LocalDateTime toDate);
    
    @Query("SELECT SUM(iu.totalCost) FROM InventoryUsage iu WHERE iu.branch.id = :branchId AND " +
           "iu.usedAt >= :fromDate AND iu.usedAt <= :toDate")
    BigDecimal calculateTotalUsageCost(@Param("branchId") Long branchId,
                                     @Param("fromDate") LocalDateTime fromDate,
                                     @Param("toDate") LocalDateTime toDate);
    
    @Query("SELECT iu.product.id, SUM(iu.quantityUsed), SUM(iu.totalCost), COUNT(iu) " +
           "FROM InventoryUsage iu WHERE iu.branch.id = :branchId AND " +
           "iu.usedAt >= :fromDate AND iu.usedAt <= :toDate " +
           "GROUP BY iu.product.id ORDER BY SUM(iu.quantityUsed) DESC")
    List<Object[]> findTopUsedProductsByBranchAndDateRange(@Param("branchId") Long branchId,
                                                          @Param("fromDate") LocalDateTime fromDate,
                                                          @Param("toDate") LocalDateTime toDate,
                                                          Pageable pageable);
    
    @Query("SELECT iu.product.id, SUM(iu.quantityUsed), SUM(iu.totalCost), COUNT(iu) " +
           "FROM InventoryUsage iu WHERE iu.usedAt >= :fromDate AND iu.usedAt <= :toDate " +
           "GROUP BY iu.product.id ORDER BY SUM(iu.quantityUsed) DESC")
    List<Object[]> findTopUsedProductsByDateRange(@Param("fromDate") LocalDateTime fromDate,
                                                 @Param("toDate") LocalDateTime toDate,
                                                 Pageable pageable);
    
    @Query("SELECT iu FROM InventoryUsage iu WHERE iu.branch.id = :branchId ORDER BY iu.usedAt DESC")
    List<InventoryUsage> findRecentUsageByBranch(@Param("branchId") Long branchId, Pageable pageable);
}
