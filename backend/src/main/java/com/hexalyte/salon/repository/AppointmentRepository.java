package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    // Find appointments by branch
    List<Appointment> findByBranchId(Long branchId);
    
    // Find appointments by staff
    List<Appointment> findByStaffId(Long staffId);
    
    // Find appointments by customer
    List<Appointment> findByCustomerId(Long customerId);
    
    // Find appointments by date
    List<Appointment> findByAppointmentDate(LocalDate date);
    
    // Find appointments by branch and date
    List<Appointment> findByBranchIdAndAppointmentDate(Long branchId, LocalDate date);
    
    // Find appointments by staff and date
    List<Appointment> findByStaffIdAndAppointmentDate(Long staffId, LocalDate date);
    
    // Find appointments by status
    List<Appointment> findByStatus(Appointment.Status status);
    
    // Find appointments by branch and status
    List<Appointment> findByBranchIdAndStatus(Long branchId, Appointment.Status status);
    
    // Find appointments by staff and status
    List<Appointment> findByStaffIdAndStatus(Long staffId, Appointment.Status status);
    
    // Find appointments by date range
    List<Appointment> findByAppointmentDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find appointments by branch and date range
    List<Appointment> findByBranchIdAndAppointmentDateBetween(Long branchId, LocalDate startDate, LocalDate endDate);
    
    // Find appointments by staff and date range
    List<Appointment> findByStaffIdAndAppointmentDateBetween(Long staffId, LocalDate startDate, LocalDate endDate);
    
    // Find appointments by customer and date range
    List<Appointment> findByCustomerIdAndAppointmentDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);
    
    // Check for time conflicts
    @Query("SELECT a FROM Appointment a WHERE a.staff.id = :staffId AND a.appointmentDate = :date " +
           "AND a.status != 'CANCELLED' AND a.status != 'NO_SHOW' " +
           "AND ((a.startTime < :endTime AND a.endTime > :startTime))")
    List<Appointment> findConflictingAppointments(@Param("staffId") Long staffId, 
                                                  @Param("date") LocalDate date,
                                                  @Param("startTime") LocalTime startTime,
                                                  @Param("endTime") LocalTime endTime);
    
    // Find appointments for availability check
    @Query("SELECT a FROM Appointment a WHERE a.staff.id = :staffId AND a.appointmentDate = :date " +
           "AND a.status != 'CANCELLED' AND a.status != 'NO_SHOW' " +
           "ORDER BY a.startTime")
    List<Appointment> findStaffAppointmentsForDate(@Param("staffId") Long staffId, @Param("date") LocalDate date);
    
    // Find today's appointments
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate = CURRENT_DATE " +
           "AND a.status IN ('BOOKED', 'IN_PROGRESS') ORDER BY a.startTime")
    List<Appointment> findTodaysAppointments();
    
    // Find appointments by branch for today
    @Query("SELECT a FROM Appointment a WHERE a.branch.id = :branchId AND a.appointmentDate = CURRENT_DATE " +
           "AND a.status IN ('BOOKED', 'IN_PROGRESS') ORDER BY a.startTime")
    List<Appointment> findTodaysAppointmentsByBranch(@Param("branchId") Long branchId);
    
    // Find upcoming appointments (next 7 days)
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN CURRENT_DATE AND :endDate " +
           "AND a.status = 'BOOKED' ORDER BY a.appointmentDate, a.startTime")
    List<Appointment> findUpcomingAppointments(@Param("endDate") LocalDate endDate);
    
    // Count appointments by status
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.status = :status")
    Long countByStatus(@Param("status") Appointment.Status status);
    
    // Count appointments by branch and status
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.branch.id = :branchId AND a.status = :status")
    Long countByBranchIdAndStatus(@Param("branchId") Long branchId, @Param("status") Appointment.Status status);
    
    // Count appointments by staff and status
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.staff.id = :staffId AND a.status = :status")
    Long countByStaffIdAndStatus(@Param("staffId") Long staffId, @Param("status") Appointment.Status status);
    
    // Find recurring appointments by parent appointment ID
    List<Appointment> findByParentAppointmentId(Long parentAppointmentId);
    
    // Find recurring appointments by parent appointment ID and status
    List<Appointment> findByParentAppointmentIdAndStatus(Long parentAppointmentId, Appointment.Status status);
}


