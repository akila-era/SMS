package com.hexalyte.salon.repository;

import com.hexalyte.salon.model.ServiceAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServiceAnalyticsRepository extends JpaRepository<ServiceAnalytics, Long> {
    
    @Query("SELECT sa FROM ServiceAnalytics sa WHERE sa.service.id = :serviceId")
    List<ServiceAnalytics> findByServiceId(@Param("serviceId") Long serviceId);
    
    @Query("SELECT sa FROM ServiceAnalytics sa WHERE sa.branch.id = :branchId")
    List<ServiceAnalytics> findByBranchId(@Param("branchId") Long branchId);
    
    List<ServiceAnalytics> findByAnalyticsDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT sa FROM ServiceAnalytics sa WHERE sa.service.id = :serviceId AND sa.analyticsDate BETWEEN :startDate AND :endDate")
    List<ServiceAnalytics> findByServiceIdAndAnalyticsDateBetween(@Param("serviceId") Long serviceId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT sa FROM ServiceAnalytics sa WHERE sa.branch.id = :branchId AND sa.analyticsDate BETWEEN :startDate AND :endDate")
    List<ServiceAnalytics> findByBranchIdAndAnalyticsDateBetween(@Param("branchId") Long branchId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT sa FROM ServiceAnalytics sa WHERE sa.analyticsDate = :date ORDER BY sa.totalRevenue DESC")
    List<ServiceAnalytics> findTopRevenueServicesForDate(@Param("date") LocalDate date);
    
    @Query("SELECT sa FROM ServiceAnalytics sa WHERE sa.analyticsDate BETWEEN :startDate AND :endDate ORDER BY sa.totalBookings DESC")
    List<ServiceAnalytics> findMostPopularServices(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT sa FROM ServiceAnalytics sa WHERE sa.analyticsDate BETWEEN :startDate AND :endDate ORDER BY sa.totalProfit DESC")
    List<ServiceAnalytics> findMostProfitableServices(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT sa FROM ServiceAnalytics sa WHERE sa.analyticsDate BETWEEN :startDate AND :endDate ORDER BY sa.averageRating DESC")
    List<ServiceAnalytics> findHighestRatedServices(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT sa FROM ServiceAnalytics sa WHERE sa.service.id = :serviceId AND sa.analyticsDate = :date")
    ServiceAnalytics findByServiceIdAndDate(@Param("serviceId") Long serviceId, @Param("date") LocalDate date);
}
