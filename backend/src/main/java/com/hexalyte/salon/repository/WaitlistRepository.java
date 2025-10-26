package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {
    
    // Find active waitlist entries by staff
    @Query("SELECT w FROM Waitlist w WHERE w.staff.id = :staffId AND w.status = :status ORDER BY w.priority DESC, w.createdAt ASC")
    List<Waitlist> findByStaffIdAndStatusOrderByPriorityDescCreatedAtAsc(@Param("staffId") Long staffId, @Param("status") Waitlist.Status status);
    
    // Find active waitlist entries by branch
    @Query("SELECT w FROM Waitlist w WHERE w.branch.id = :branchId AND w.status = :status ORDER BY w.priority DESC, w.createdAt ASC")
    List<Waitlist> findByBranchIdAndStatusOrderByPriorityDescCreatedAtAsc(@Param("branchId") Long branchId, @Param("status") Waitlist.Status status);
    
    // Find active waitlist entries by customer
    @Query("SELECT w FROM Waitlist w WHERE w.customer.id = :customerId AND w.status = :status ORDER BY w.createdAt DESC")
    List<Waitlist> findByCustomerIdAndStatusOrderByCreatedAtDesc(@Param("customerId") Long customerId, @Param("status") Waitlist.Status status);
    
    // Find waitlist entries by customer and status
    List<Waitlist> findByCustomerIdAndStatus(Long customerId, Waitlist.Status status);
    
    // Find waitlist entries by status
    List<Waitlist> findByStatusOrderByPriorityDescCreatedAtAsc(Waitlist.Status status);
    
    // Find waitlist entries that match a specific time slot
    @Query("SELECT w FROM Waitlist w WHERE w.staff.id = :staffId AND w.status = 'ACTIVE' " +
           "AND w.preferredDate = :date " +
           "AND ((w.preferredStartTime <= :endTime AND w.preferredEndTime >= :startTime) " +
           "OR (w.flexibleDays > 0 AND ABS(DATEDIFF(w.preferredDate, :date)) <= w.flexibleDays)) " +
           "ORDER BY w.priority DESC, w.createdAt ASC")
    List<Waitlist> findMatchingWaitlistEntries(@Param("staffId") Long staffId, 
                                              @Param("date") LocalDate date,
                                              @Param("startTime") LocalTime startTime,
                                              @Param("endTime") LocalTime endTime);
    
    // Find waitlist entries that are about to expire
    @Query("SELECT w FROM Waitlist w WHERE w.status = 'ACTIVE' AND w.expiresAt <= :now")
    List<Waitlist> findExpiredWaitlistEntries(@Param("now") LocalDateTime now);
    
    // Find waitlist entries by date range
    @Query("SELECT w FROM Waitlist w WHERE w.preferredDate BETWEEN :startDate AND :endDate " +
           "AND w.status = 'ACTIVE' ORDER BY w.priority DESC, w.createdAt ASC")
    List<Waitlist> findWaitlistEntriesByDateRange(@Param("startDate") LocalDate startDate, 
                                                 @Param("endDate") LocalDate endDate);
    
    // Count active waitlist entries by staff
    Long countByStaffIdAndStatus(Long staffId, Waitlist.Status status);
    
    // Count active waitlist entries by branch
    Long countByBranchIdAndStatus(Long branchId, Waitlist.Status status);
    
    // Count waitlist entries by status
    Long countByStatus(Waitlist.Status status);
}
