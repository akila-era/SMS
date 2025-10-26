package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.ServiceProductUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProductUsageRepository extends JpaRepository<ServiceProductUsage, Long> {
    
    List<ServiceProductUsage> findByServiceId(Long serviceId);
    
    List<ServiceProductUsage> findByProductNameContainingIgnoreCase(String productName);
    
    List<ServiceProductUsage> findByProductSku(String productSku);
    
    List<ServiceProductUsage> findByIsRequiredTrue();
    
    List<ServiceProductUsage> findByServiceIdAndIsRequiredTrue(Long serviceId);
    
    @Query("SELECT spu FROM ServiceProductUsage spu WHERE spu.service.id = :serviceId ORDER BY spu.productName")
    List<ServiceProductUsage> findByServiceIdOrderByProductName(@Param("serviceId") Long serviceId);
    
    @Query("SELECT spu FROM ServiceProductUsage spu WHERE spu.productName = :productName AND spu.isRequired = true")
    List<ServiceProductUsage> findRequiredUsagesForProduct(@Param("productName") String productName);
}
