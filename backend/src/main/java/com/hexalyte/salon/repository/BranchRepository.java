package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByIsActiveTrue();
    List<Branch> findByIsActive(Boolean isActive);
}


