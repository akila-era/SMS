package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query("SELECT s FROM Staff s WHERE s.branch.id = :branchId")
    List<Staff> findByBranchId(@Param("branchId") Long branchId);
    List<Staff> findByStatus(Staff.StaffStatus status);
    @Query("SELECT s FROM Staff s WHERE s.branch.id = :branchId AND s.status = :status")
    List<Staff> findByBranchIdAndStatus(@Param("branchId") Long branchId, @Param("status") Staff.StaffStatus status);
    
    @Query("SELECT s FROM Staff s WHERE s.branch.id = :branchId AND s.status = 'ACTIVE'")
    List<Staff> findActiveStaffByBranch(@Param("branchId") Long branchId);
    
    @Query("SELECT COUNT(s) FROM Staff s WHERE s.branch.id = :branchId")
    Long countByBranchId(@Param("branchId") Long branchId);
    
    // New methods for advanced staff management
    List<Staff> findByDesignation(String designation);
    List<Staff> findBySkillSetContainingIgnoreCase(String skill);
    boolean existsByEmployeeCode(String employeeCode);
    Staff findByEmployeeCode(String employeeCode);
    
    @Query("SELECT s FROM Staff s WHERE s.status = :status AND s.branch.id = :branchId")
    List<Staff> findByStatusAndBranchId(@Param("status") Staff.StaffStatus status, @Param("branchId") Long branchId);
    
    @Query("SELECT s FROM Staff s WHERE s.designation = :designation AND s.branch.id = :branchId")
    List<Staff> findByDesignationAndBranchId(@Param("designation") String designation, @Param("branchId") Long branchId);
    
    @Query("SELECT s FROM Staff s WHERE s.skillSet LIKE %:skill% AND s.branch.id = :branchId")
    List<Staff> findBySkillAndBranchId(@Param("skill") String skill, @Param("branchId") Long branchId);
    
    @Query("SELECT s FROM Staff s WHERE s.salaryType = :salaryType")
    List<Staff> findBySalaryType(@Param("salaryType") Staff.SalaryType salaryType);
    
    @Query("SELECT s FROM Staff s WHERE s.salaryType = :salaryType AND s.branch.id = :branchId")
    List<Staff> findBySalaryTypeAndBranchId(@Param("salaryType") Staff.SalaryType salaryType, @Param("branchId") Long branchId);
}


