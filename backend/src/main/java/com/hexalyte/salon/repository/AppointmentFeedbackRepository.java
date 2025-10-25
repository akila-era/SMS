package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.AppointmentFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentFeedbackRepository extends JpaRepository<AppointmentFeedback, Long> {
    
    // Find feedback by appointment
    Optional<AppointmentFeedback> findByAppointmentId(Long appointmentId);
    
    // Find feedback by appointment and customer
    @Query("SELECT f FROM AppointmentFeedback f WHERE f.appointment.id = :appointmentId " +
           "AND f.appointment.customer.id = :customerId")
    Optional<AppointmentFeedback> findByAppointmentIdAndCustomerId(@Param("appointmentId") Long appointmentId, 
                                                                  @Param("customerId") Long customerId);
    
    // Find feedback by staff
    @Query("SELECT f FROM AppointmentFeedback f WHERE f.appointment.staff.id = :staffId " +
           "ORDER BY f.createdAt DESC")
    List<AppointmentFeedback> findByStaffId(@Param("staffId") Long staffId);
    
    // Find feedback by branch
    @Query("SELECT f FROM AppointmentFeedback f WHERE f.appointment.branch.id = :branchId " +
           "ORDER BY f.createdAt DESC")
    List<AppointmentFeedback> findByBranchId(@Param("branchId") Long branchId);
    
    // Find feedback by date range
    @Query("SELECT f FROM AppointmentFeedback f WHERE f.appointment.appointmentDate BETWEEN :startDate AND :endDate " +
           "ORDER BY f.createdAt DESC")
    List<AppointmentFeedback> findByDateRange(@Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate);
    
    // Find feedback by rating range
    @Query("SELECT f FROM AppointmentFeedback f WHERE f.overallRating BETWEEN :minRating AND :maxRating " +
           "ORDER BY f.createdAt DESC")
    List<AppointmentFeedback> findByRatingRange(@Param("minRating") Integer minRating, 
                                              @Param("maxRating") Integer maxRating);
    
    // Find feedback by would recommend
    List<AppointmentFeedback> findByWouldRecommendTrue();
    
    // Find feedback by anonymous status
    List<AppointmentFeedback> findByIsAnonymousTrue();
    
    // Calculate average rating by staff
    @Query("SELECT AVG(f.overallRating) FROM AppointmentFeedback f WHERE f.appointment.staff.id = :staffId")
    Double getAverageRatingByStaff(@Param("staffId") Long staffId);
    
    // Calculate average rating by branch
    @Query("SELECT AVG(f.overallRating) FROM AppointmentFeedback f WHERE f.appointment.branch.id = :branchId")
    Double getAverageRatingByBranch(@Param("branchId") Long branchId);
    
    // Count feedback by rating
    @Query("SELECT COUNT(f) FROM AppointmentFeedback f WHERE f.overallRating = :rating")
    Long countByRating(@Param("rating") Integer rating);
    
    // Count feedback by staff and rating
    @Query("SELECT COUNT(f) FROM AppointmentFeedback f WHERE f.appointment.staff.id = :staffId " +
           "AND f.overallRating = :rating")
    Long countByStaffIdAndRating(@Param("staffId") Long staffId, @Param("rating") Integer rating);
    
    // Count feedback by branch and rating
    @Query("SELECT COUNT(f) FROM AppointmentFeedback f WHERE f.appointment.branch.id = :branchId " +
           "AND f.overallRating = :rating")
    Long countByBranchIdAndRating(@Param("branchId") Long branchId, @Param("rating") Integer rating);
    
    // Get recent feedback (last 30 days)
    @Query("SELECT f FROM AppointmentFeedback f WHERE f.createdAt >= CURRENT_DATE - 30 " +
           "ORDER BY f.createdAt DESC")
    List<AppointmentFeedback> findRecentFeedback();
}
