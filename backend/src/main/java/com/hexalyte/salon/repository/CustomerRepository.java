package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByPhone(String phone);
    List<Customer> findByEmail(String email);
    
    @Query("SELECT c FROM Customer c WHERE c.firstName LIKE %:name% OR c.lastName LIKE %:name% OR c.phone LIKE %:name%")
    List<Customer> searchByNameOrPhone(@Param("name") String name);
    
    @Query("SELECT c FROM Customer c WHERE c.membershipLevel = :level")
    List<Customer> findByMembershipLevel(@Param("level") Customer.MembershipLevel level);
    
    @Query("SELECT c FROM Customer c ORDER BY c.totalSpent DESC")
    List<Customer> findTopCustomers();
    
    @Query("SELECT c FROM Customer c WHERE c.loyaltyPoints >= :minPoints")
    List<Customer> findByLoyaltyPointsGreaterThanEqual(@Param("minPoints") Integer minPoints);
}


