package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.ServiceBranchConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceBranchConfigurationRepository extends JpaRepository<ServiceBranchConfiguration, Long> {
    
    @Query("SELECT sbc FROM ServiceBranchConfiguration sbc WHERE sbc.service.id = :serviceId")
    List<ServiceBranchConfiguration> findByServiceId(@Param("serviceId") Long serviceId);
    
    @Query("SELECT sbc FROM ServiceBranchConfiguration sbc WHERE sbc.branch.id = :branchId")
    List<ServiceBranchConfiguration> findByBranchId(@Param("branchId") Long branchId);
    
    @Query("SELECT sbc FROM ServiceBranchConfiguration sbc WHERE sbc.service.id = :serviceId AND sbc.branch.id = :branchId")
    Optional<ServiceBranchConfiguration> findByServiceIdAndBranchId(@Param("serviceId") Long serviceId, @Param("branchId") Long branchId);
    
    @Query("SELECT sbc FROM ServiceBranchConfiguration sbc WHERE sbc.service.id = :serviceId AND sbc.isEnabled = true")
    List<ServiceBranchConfiguration> findByServiceIdAndIsEnabledTrue(@Param("serviceId") Long serviceId);
    
    @Query("SELECT sbc FROM ServiceBranchConfiguration sbc WHERE sbc.branch.id = :branchId AND sbc.isEnabled = true")
    List<ServiceBranchConfiguration> findByBranchIdAndIsEnabledTrue(@Param("branchId") Long branchId);
    
    @Query("SELECT sbc FROM ServiceBranchConfiguration sbc WHERE sbc.service.id = :serviceId AND sbc.branch.id = :branchId AND sbc.isEnabled = true")
    Optional<ServiceBranchConfiguration> findActiveConfiguration(@Param("serviceId") Long serviceId, @Param("branchId") Long branchId);
    
    @Query("SELECT sbc FROM ServiceBranchConfiguration sbc WHERE sbc.branch.id = :branchId AND sbc.isEnabled = true ORDER BY sbc.service.name")
    List<ServiceBranchConfiguration> findActiveServicesForBranch(@Param("branchId") Long branchId);
    
    @Query("SELECT sbc FROM ServiceBranchConfiguration sbc WHERE sbc.service.id = :serviceId AND sbc.isEnabled = true ORDER BY sbc.branch.name")
    List<ServiceBranchConfiguration> findActiveBranchesForService(@Param("serviceId") Long serviceId);
}
