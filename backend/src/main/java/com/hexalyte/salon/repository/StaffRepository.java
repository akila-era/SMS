package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByBranchId(Long branchId);
    List<Staff> findByIsActiveTrue();
    List<Staff> findByBranchIdAndIsActiveTrue(Long branchId);
    
    @Query("SELECT s FROM Staff s WHERE s.branch.id = :branchId AND s.isActive = true")
    List<Staff> findActiveStaffByBranch(@Param("branchId") Long branchId);
    
    @Query("SELECT COUNT(s) FROM Staff s WHERE s.branch.id = :branchId")
    Long countByBranchId(@Param("branchId") Long branchId);
}


