package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.BranchUserAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchUserAccessRepository extends JpaRepository<BranchUserAccess, Long> {
    
    // Find access by user and branch
    @Query("SELECT bua FROM BranchUserAccess bua WHERE bua.user.id = :userId AND bua.branch.id = :branchId")
    Optional<BranchUserAccess> findByUserIdAndBranchId(@Param("userId") Long userId, @Param("branchId") Long branchId);
    
    // Find all access for a user
    @Query("SELECT bua FROM BranchUserAccess bua WHERE bua.user.id = :userId")
    List<BranchUserAccess> findByUserId(@Param("userId") Long userId);
    
    // Find all access for a branch
    @Query("SELECT bua FROM BranchUserAccess bua WHERE bua.branch.id = :branchId")
    List<BranchUserAccess> findByBranchId(@Param("branchId") Long branchId);
    
    // Find access by user, branch and role
    @Query("SELECT bua FROM BranchUserAccess bua WHERE bua.user.id = :userId AND bua.branch.id = :branchId AND bua.role = :role")
    Optional<BranchUserAccess> findByUserIdAndBranchIdAndRole(@Param("userId") Long userId, @Param("branchId") Long branchId, @Param("role") BranchUserAccess.AccessRole role);
    
    // Find users with specific role in a branch
    @Query("SELECT bua FROM BranchUserAccess bua WHERE bua.branch.id = :branchId AND bua.role = :role")
    List<BranchUserAccess> findByBranchIdAndRole(@Param("branchId") Long branchId, @Param("role") BranchUserAccess.AccessRole role);
    
    // Find branches accessible by user
    @Query("SELECT bua.branch FROM BranchUserAccess bua WHERE bua.user.id = :userId")
    List<Object> findBranchesByUserId(@Param("userId") Long userId);
    
    // Find users with full access to a branch
    @Query("SELECT bua FROM BranchUserAccess bua WHERE bua.branch.id = :branchId AND bua.accessLevel = :accessLevel")
    List<BranchUserAccess> findByBranchIdAndAccessLevel(@Param("branchId") Long branchId, @Param("accessLevel") BranchUserAccess.AccessLevel accessLevel);
    
    // Check if user has access to branch
    @Query("SELECT COUNT(bua) > 0 FROM BranchUserAccess bua WHERE bua.user.id = :userId AND bua.branch.id = :branchId")
    boolean existsByUserIdAndBranchId(@Param("userId") Long userId, @Param("branchId") Long branchId);
    
    // Check if user has specific role in branch
    @Query("SELECT COUNT(bua) > 0 FROM BranchUserAccess bua WHERE bua.user.id = :userId AND bua.branch.id = :branchId AND bua.role = :role")
    boolean existsByUserIdAndBranchIdAndRole(@Param("userId") Long userId, @Param("branchId") Long branchId, @Param("role") BranchUserAccess.AccessRole role);
    
    // Find users who can manage staff in a branch
    @Query("SELECT bua FROM BranchUserAccess bua WHERE bua.branch.id = :branchId AND bua.canManageStaff = true")
    List<BranchUserAccess> findByBranchIdAndCanManageStaffTrue(@Param("branchId") Long branchId);
    
    // Find users who can view financial reports in a branch
    @Query("SELECT bua FROM BranchUserAccess bua WHERE bua.branch.id = :branchId AND bua.canViewFinancialReports = true")
    List<BranchUserAccess> findByBranchIdAndCanViewFinancialReportsTrue(@Param("branchId") Long branchId);
    
    // Delete access by user and branch
    @Query("DELETE FROM BranchUserAccess bua WHERE bua.user.id = :userId AND bua.branch.id = :branchId")
    void deleteByUserIdAndBranchId(@Param("userId") Long userId, @Param("branchId") Long branchId);
    
    // Count users in a branch by role
    @Query("SELECT COUNT(bua) FROM BranchUserAccess bua WHERE bua.branch.id = :branchId AND bua.role = :role")
    Long countByBranchIdAndRole(@Param("branchId") Long branchId, @Param("role") BranchUserAccess.AccessRole role);
    
    // Delete all access for a branch
    @Query("DELETE FROM BranchUserAccess bua WHERE bua.branch.id = :branchId")
    void deleteByBranchId(@Param("branchId") Long branchId);
}
