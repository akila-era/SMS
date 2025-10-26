package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    List<Supplier> findByStatus(Supplier.SupplierStatus status);
    
    @Query("SELECT s FROM Supplier s WHERE " +
           "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:status IS NULL OR s.status = :status)")
    Page<Supplier> findByFilters(@Param("name") String name,
                                @Param("status") Supplier.SupplierStatus status,
                                Pageable pageable);
    
    @Query("SELECT s FROM Supplier s WHERE s.status = 'ACTIVE' ORDER BY s.name")
    List<Supplier> findActiveSuppliers();
    
    boolean existsByEmail(String email);
    
    boolean existsByEmailAndSupplierIdNot(String email, Long supplierId);
}
