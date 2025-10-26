package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.ServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {
    
    List<ServicePackage> findByIsActiveTrue();
    
    List<ServicePackage> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT sp FROM ServicePackage sp WHERE sp.isActive = true AND (sp.name LIKE %:searchTerm% OR sp.description LIKE %:searchTerm%)")
    List<ServicePackage> searchActivePackages(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT sp FROM ServicePackage sp WHERE sp.isActive = true ORDER BY sp.name")
    List<ServicePackage> findAllActiveOrderByName();
    
    @Query("SELECT sp FROM ServicePackage sp WHERE sp.canSplitSessions = true AND sp.isActive = true")
    List<ServicePackage> findSplittablePackages();
}
