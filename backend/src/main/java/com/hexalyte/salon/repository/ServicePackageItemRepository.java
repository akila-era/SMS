package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.ServicePackageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicePackageItemRepository extends JpaRepository<ServicePackageItem, Long> {
    
    @Query("SELECT spi FROM ServicePackageItem spi WHERE spi.servicePackage.id = :servicePackageId")
    List<ServicePackageItem> findByServicePackageId(@Param("servicePackageId") Long servicePackageId);
    
    @Query("SELECT spi FROM ServicePackageItem spi WHERE spi.service.id = :serviceId")
    List<ServicePackageItem> findByServiceId(@Param("serviceId") Long serviceId);
    
    @Query("SELECT spi FROM ServicePackageItem spi WHERE spi.servicePackage.id = :servicePackageId ORDER BY spi.sequenceOrder")
    List<ServicePackageItem> findByServicePackageIdOrderBySequenceOrder(@Param("servicePackageId") Long servicePackageId);
    
    @Query("SELECT spi FROM ServicePackageItem spi WHERE spi.servicePackage.id = :packageId AND spi.isOptional = false ORDER BY spi.sequenceOrder")
    List<ServicePackageItem> findRequiredItemsForPackage(@Param("packageId") Long packageId);
    
    @Query("SELECT spi FROM ServicePackageItem spi WHERE spi.servicePackage.id = :packageId AND spi.isOptional = true ORDER BY spi.sequenceOrder")
    List<ServicePackageItem> findOptionalItemsForPackage(@Param("packageId") Long packageId);
    
    @Query("SELECT spi FROM ServicePackageItem spi WHERE spi.service.id = :serviceId AND spi.servicePackage.isActive = true")
    List<ServicePackageItem> findActivePackagesContainingService(@Param("serviceId") Long serviceId);
}
