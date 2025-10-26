package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.ServiceProductMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceProductMappingRepository extends JpaRepository<ServiceProductMapping, Long> {
    
    @Query("SELECT spm FROM ServiceProductMapping spm WHERE spm.service.id = :serviceId")
    List<ServiceProductMapping> findByServiceId(@Param("serviceId") Long serviceId);
    
    @Query("SELECT spm FROM ServiceProductMapping spm WHERE spm.product.id = :productId")
    List<ServiceProductMapping> findByProductId(@Param("productId") Long productId);
    
    @Query("SELECT spm FROM ServiceProductMapping spm WHERE spm.service.id = :serviceId AND spm.product.id = :productId")
    Optional<ServiceProductMapping> findByServiceIdAndProductId(@Param("serviceId") Long serviceId, @Param("productId") Long productId);
    
    @Query("SELECT spm FROM ServiceProductMapping spm JOIN FETCH spm.product p WHERE spm.service.id = :serviceId")
    List<ServiceProductMapping> findByServiceIdWithProduct(@Param("serviceId") Long serviceId);
    
    @Query("SELECT spm FROM ServiceProductMapping spm JOIN FETCH spm.service s WHERE spm.product.id = :productId")
    List<ServiceProductMapping> findByProductIdWithService(@Param("productId") Long productId);
    
    @Query("SELECT spm FROM ServiceProductMapping spm WHERE spm.service.id IN :serviceIds")
    List<ServiceProductMapping> findByServiceIdIn(@Param("serviceIds") List<Long> serviceIds);
    
    @Query("DELETE FROM ServiceProductMapping spm WHERE spm.service.id = :serviceId")
    void deleteByServiceId(@Param("serviceId") Long serviceId);
    
    @Query("DELETE FROM ServiceProductMapping spm WHERE spm.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
    
    @Query("DELETE FROM ServiceProductMapping spm WHERE spm.service.id = :serviceId AND spm.product.id = :productId")
    void deleteByServiceIdAndProductId(@Param("serviceId") Long serviceId, @Param("productId") Long productId);
}
