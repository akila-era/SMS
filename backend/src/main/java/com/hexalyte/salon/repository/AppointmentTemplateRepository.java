package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.AppointmentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentTemplateRepository extends JpaRepository<AppointmentTemplate, Long> {
    
    // Find templates by branch
    List<AppointmentTemplate> findByBranchIdAndIsActiveTrueOrderByUsageCountDesc(Long branchId);
    
    // Find all active templates
    List<AppointmentTemplate> findByIsActiveTrueOrderByUsageCountDesc();
    
    // Find templates by name (case insensitive)
    List<AppointmentTemplate> findByTemplateNameContainingIgnoreCaseAndIsActiveTrue(String templateName);
    
    // Find templates by branch and name
    List<AppointmentTemplate> findByBranchIdAndTemplateNameContainingIgnoreCaseAndIsActiveTrue(
            Long branchId, String templateName);
    
    // Find most popular templates
    @Query("SELECT t FROM AppointmentTemplate t WHERE t.isActive = true ORDER BY t.usageCount DESC")
    List<AppointmentTemplate> findMostPopularTemplates();
    
    // Find most popular templates by branch
    @Query("SELECT t FROM AppointmentTemplate t WHERE t.branch.id = :branchId AND t.isActive = true " +
           "ORDER BY t.usageCount DESC")
    List<AppointmentTemplate> findMostPopularTemplatesByBranch(@Param("branchId") Long branchId);
    
    // Count templates by branch
    Long countByBranchIdAndIsActiveTrue(Long branchId);
    
    // Count all active templates
    Long countByIsActiveTrue();
}
