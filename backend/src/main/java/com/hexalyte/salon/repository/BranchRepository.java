package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    
    // Find branches by status
    List<Branch> findByStatus(Branch.BranchStatus status);
    
    // Find active branches
    List<Branch> findByStatusOrderByBranchNameAsc(Branch.BranchStatus status);
    
    // Find branch by code
    Optional<Branch> findByBranchCode(String branchCode);
    
    // Check if branch code exists
    boolean existsByBranchCode(String branchCode);
    
    // Find branches by city
    List<Branch> findByCityOrderByBranchNameAsc(String city);
    
    // Find branches by manager
    @Query("SELECT b FROM Branch b WHERE b.manager.id = :managerId")
    List<Branch> findByManagerId(@Param("managerId") Long managerId);
    
    // Find branches with no manager
    List<Branch> findByManagerIsNull();
    
    // Search branches by name or code
    @Query("SELECT b FROM Branch b WHERE " +
           "LOWER(b.branchName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.branchCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Branch> searchBranches(@Param("searchTerm") String searchTerm);
    
    // Get branch statistics
    @Query("SELECT COUNT(b) FROM Branch b WHERE b.status = :status")
    Long countByStatus(@Param("status") Branch.BranchStatus status);
    
    // Find branches created by user
    @Query("SELECT b FROM Branch b WHERE b.createdBy.id = :createdById")
    List<Branch> findByCreatedById(@Param("createdById") Long createdById);
}


