package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long> {
    List<Commission> findByStaffId(Long staffId);
    List<Commission> findByBranchId(Long branchId);
    List<Commission> findByStatus(Commission.Status status);
    List<Commission> findByCommissionDateBetween(LocalDate startDate, LocalDate endDate);
    List<Commission> findByStaffIdAndCommissionDateBetween(Long staffId, LocalDate startDate, LocalDate endDate);
    List<Commission> findByBranchIdAndCommissionDateBetween(Long branchId, LocalDate startDate, LocalDate endDate);
}


