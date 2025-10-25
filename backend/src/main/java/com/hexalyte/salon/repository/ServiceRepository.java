package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByIsActiveTrue();
    List<Service> findByCategory(String category);
    List<Service> findByCategoryAndIsActiveTrue(String category);
    
    @Query("SELECT DISTINCT s.category FROM Service s WHERE s.isActive = true")
    List<String> findDistinctCategories();
    
    @Query("SELECT s FROM Service s WHERE s.name LIKE %:name% AND s.isActive = true")
    List<Service> findByNameContaining(@Param("name") String name);
}


