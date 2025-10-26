package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.Attendance;
import com.hexalyte.salon.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    @Query("SELECT a FROM Attendance a WHERE a.staff.id = :staffId")
    List<Attendance> findByStaffId(@Param("staffId") Long staffId);
    
    @Query("SELECT a FROM Attendance a WHERE a.staff.id = :staffId AND a.workDate BETWEEN :startDate AND :endDate")
    List<Attendance> findByStaffIdAndWorkDateBetween(@Param("staffId") Long staffId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Attendance a WHERE a.staff.id = :staffId AND a.workDate = :workDate")
    Optional<Attendance> findByStaffIdAndWorkDate(@Param("staffId") Long staffId, @Param("workDate") LocalDate workDate);
    
    List<Attendance> findByWorkDate(LocalDate workDate);
    
    List<Attendance> findByWorkDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT a FROM Attendance a WHERE a.staff.branch.id = :branchId AND a.workDate = :workDate")
    List<Attendance> findByBranchIdAndWorkDate(@Param("branchId") Long branchId, @Param("workDate") LocalDate workDate);
    
    @Query("SELECT a FROM Attendance a WHERE a.staff.branch.id = :branchId AND a.workDate BETWEEN :startDate AND :endDate")
    List<Attendance> findByBranchIdAndWorkDateBetween(@Param("branchId") Long branchId, 
                                                     @Param("startDate") LocalDate startDate, 
                                                     @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.staff.id = :staffId AND a.workDate BETWEEN :startDate AND :endDate AND a.status = 'PRESENT'")
    Long countPresentDaysByStaffAndDateRange(@Param("staffId") Long staffId, 
                                           @Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.staff.id = :staffId AND a.workDate BETWEEN :startDate AND :endDate AND a.status = 'ABSENT'")
    Long countAbsentDaysByStaffAndDateRange(@Param("staffId") Long staffId, 
                                          @Param("startDate") LocalDate startDate, 
                                          @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(a.totalHours) FROM Attendance a WHERE a.staff.id = :staffId AND a.workDate BETWEEN :startDate AND :endDate")
    Double sumTotalHoursByStaffAndDateRange(@Param("staffId") Long staffId, 
                                          @Param("startDate") LocalDate startDate, 
                                          @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(a.overtimeHours) FROM Attendance a WHERE a.staff.id = :staffId AND a.workDate BETWEEN :startDate AND :endDate")
    Double sumOvertimeHoursByStaffAndDateRange(@Param("staffId") Long staffId, 
                                             @Param("startDate") LocalDate startDate, 
                                             @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Attendance a WHERE a.staff.id = :staffId AND a.workDate = :workDate AND a.checkIn IS NOT NULL AND a.checkOut IS NULL")
    Optional<Attendance> findActiveCheckInByStaffAndDate(@Param("staffId") Long staffId, @Param("workDate") LocalDate workDate);
    
    @Query("SELECT COUNT(a) > 0 FROM Attendance a WHERE a.staff.id = :staffId AND a.workDate = :workDate")
    boolean existsByStaffIdAndWorkDate(@Param("staffId") Long staffId, @Param("workDate") LocalDate workDate);
}
