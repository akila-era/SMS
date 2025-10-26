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
    @Query("SELECT at FROM AppointmentTemplate at WHERE at.branch.id = :branchId AND at.isActive = true ORDER BY at.usageCount DESC")
    List<AppointmentTemplate> findByBranchIdAndIsActiveTrueOrderByUsageCountDesc(@Param("branchId") Long branchId);
    
    // Find all active templates
    List<AppointmentTemplate> findByIsActiveTrueOrderByUsageCountDesc();
    
    // Find templates by name (case insensitive)
    List<AppointmentTemplate> findByTemplateNameContainingIgnoreCaseAndIsActiveTrue(String templateName);
    
    // Find templates by branch and name
    @Query("SELECT at FROM AppointmentTemplate at WHERE at.branch.id = :branchId AND LOWER(at.templateName) LIKE LOWER(CONCAT('%', :templateName, '%')) AND at.isActive = true")
    List<AppointmentTemplate> findByBranchIdAndTemplateNameContainingIgnoreCaseAndIsActiveTrue(
            @Param("branchId") Long branchId, @Param("templateName") String templateName);
    
    // Find most popular templates
    @Query("SELECT t FROM AppointmentTemplate t WHERE t.isActive = true ORDER BY t.usageCount DESC")
    List<AppointmentTemplate> findMostPopularTemplates();
    
    // Find most popular templates by branch
    @Query("SELECT t FROM AppointmentTemplate t WHERE t.branch.id = :branchId AND t.isActive = true " +
           "ORDER BY t.usageCount DESC")
    List<AppointmentTemplate> findMostPopularTemplatesByBranch(@Param("branchId") Long branchId);
    
    // Count templates by branch
    @Query("SELECT COUNT(at) FROM AppointmentTemplate at WHERE at.branch.id = :branchId AND at.isActive = true")
    Long countByBranchIdAndIsActiveTrue(@Param("branchId") Long branchId);
    
    // Count all active templates
    Long countByIsActiveTrue();
}
