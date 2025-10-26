package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Inventory;
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
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    @Query("SELECT i FROM Inventory i WHERE i.branch.id = :branchId AND i.product.id = :productId")
    Optional<Inventory> findByBranchIdAndProductId(@Param("branchId") Long branchId, @Param("productId") Long productId);
    
    @Query("SELECT i FROM Inventory i WHERE i.branch.id = :branchId")
    List<Inventory> findByBranchId(@Param("branchId") Long branchId);
    
    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId")
    List<Inventory> findByProductId(@Param("productId") Long productId);
    
    @Query("SELECT i FROM Inventory i JOIN FETCH i.product p WHERE i.branch.id = :branchId AND " +
           "(:productName IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%'))) AND " +
           "(:category IS NULL OR p.category = :category) AND " +
           "(:productType IS NULL OR p.productType = :productType)")
    Page<Inventory> findByBranchIdAndFilters(@Param("branchId") Long branchId,
                                           @Param("productName") String productName,
                                           @Param("category") String category,
                                           @Param("productType") String productType,
                                           Pageable pageable);
    
    @Query("SELECT i FROM Inventory i JOIN FETCH i.product p WHERE " +
           "(:branchId IS NULL OR i.branch.id = :branchId) AND " +
           "(:productName IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%'))) AND " +
           "(:category IS NULL OR p.category = :category) AND " +
           "(:productType IS NULL OR p.productType = :productType)")
    Page<Inventory> findByFilters(@Param("branchId") Long branchId,
                                @Param("productName") String productName,
                                @Param("category") String category,
                                @Param("productType") String productType,
                                Pageable pageable);
    
    @Query("SELECT i FROM Inventory i JOIN FETCH i.product p WHERE i.branch.id = :branchId AND " +
           "i.quantity <= p.alertQuantity AND p.alertQuantity > 0")
    List<Inventory> findLowStockItemsByBranch(@Param("branchId") Long branchId);
    
    @Query("SELECT i FROM Inventory i JOIN FETCH i.product p WHERE " +
           "i.quantity <= p.alertQuantity AND p.alertQuantity > 0")
    List<Inventory> findLowStockItems();
    
    @Query("SELECT i FROM Inventory i JOIN FETCH i.product p WHERE i.branch.id = :branchId AND i.quantity = 0")
    List<Inventory> findOutOfStockItemsByBranch(@Param("branchId") Long branchId);
    
    @Query("SELECT i FROM Inventory i JOIN FETCH i.product p WHERE i.quantity = 0")
    List<Inventory> findOutOfStockItems();
    
    @Query("SELECT SUM(i.quantity * p.costPrice) FROM Inventory i JOIN i.product p WHERE i.branch.id = :branchId")
    BigDecimal calculateTotalValueByBranch(@Param("branchId") Long branchId);
    
    @Query("SELECT SUM(i.quantity * p.costPrice) FROM Inventory i JOIN i.product p")
    BigDecimal calculateTotalValue();
    
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.branch.id = :branchId")
    Long countByBranchId(@Param("branchId") Long branchId);
    
    @Query("SELECT COUNT(i) FROM Inventory i JOIN i.product p WHERE i.branch.id = :branchId AND " +
           "i.quantity <= p.alertQuantity AND p.alertQuantity > 0")
    Long countLowStockByBranchId(@Param("branchId") Long branchId);
    
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.branch.id = :branchId AND i.quantity = 0")
    Long countOutOfStockByBranchId(@Param("branchId") Long branchId);
}
