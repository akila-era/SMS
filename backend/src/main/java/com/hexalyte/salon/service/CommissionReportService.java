package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.CommissionReportDTO;
import com.hexalyte.salon.model.CommissionSummary;
import com.hexalyte.salon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommissionReportService {
    
    @Autowired
    private CommissionRepository commissionRepository;
    
    @Autowired
    private CommissionSummaryRepository commissionSummaryRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private BranchRepository branchRepository;

    /**
     * Generate branch-wise commission report for a month
     */
    public CommissionReportDTO generateBranchWiseReport(String month) {
        CommissionReportDTO report = new CommissionReportDTO("BRANCH_WISE", month);
        
        // Get branch-wise commission data
        List<Object[]> branchData = commissionSummaryRepository.findBranchWiseCommissionInMonth(month);
        
        List<CommissionReportDTO.BranchCommissionSummary> branchSummaries = new ArrayList<>();
        for (Object[] data : branchData) {
            Long branchId = (Long) data[0];
            String branchName = (String) data[1];
            BigDecimal totalCommission = (BigDecimal) data[2];
            Long totalServices = (Long) data[3];
            
            CommissionReportDTO.BranchCommissionSummary summary = new CommissionReportDTO.BranchCommissionSummary(
                branchId, branchName, month, totalServices.intValue(), totalCommission
            );
            branchSummaries.add(summary);
        }
        
        report.setBranchSummaries(branchSummaries);
        return report;
    }
    
    /**
     * Generate staff-wise commission report for a month
     */
    public CommissionReportDTO generateStaffWiseReport(String month) {
        CommissionReportDTO report = new CommissionReportDTO("STAFF_WISE", month);
        
        // Get staff-wise commission data
        List<Object[]> staffData = commissionSummaryRepository.findTopEarningStaffInMonth(
            month, PageRequest.of(0, 1000) // Get all staff
        );
        
        List<CommissionReportDTO.StaffCommissionSummary> staffSummaries = new ArrayList<>();
        for (Object[] data : staffData) {
            Long staffId = (Long) data[0];
            String firstName = (String) data[1];
            String lastName = (String) data[2];
            String branchName = (String) data[3];
            BigDecimal totalCommission = (BigDecimal) data[4];
            Long totalServices = (Long) data[5];
            
            CommissionReportDTO.StaffCommissionSummary summary = new CommissionReportDTO.StaffCommissionSummary(
                staffId, firstName + " " + lastName, null, // employee code not available in this query
                null, branchName, month, totalServices.intValue(), totalCommission
            );
            staffSummaries.add(summary);
        }
        
        report.setStaffSummaries(staffSummaries);
        return report;
    }
    
    /**
     * Generate comprehensive commission dashboard report
     */
    public CommissionReportDTO generateDashboardReport() {
        String currentMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        CommissionReportDTO report = new CommissionReportDTO("SUMMARY", currentMonth);
        
        // Create dashboard data
        CommissionReportDTO.CommissionDashboardData dashboardData = new CommissionReportDTO.CommissionDashboardData();
        
        // Total commissions this month
        BigDecimal totalCommissionsThisMonth = commissionSummaryRepository.calculateTotalCommissionForMonth(currentMonth);
        dashboardData.setTotalCommissionsThisMonth(totalCommissionsThisMonth);
        
        // Total pending approvals
        Long totalPendingApprovals = commissionSummaryRepository.countPendingSummaries();
        dashboardData.setTotalPendingApprovals(totalPendingApprovals.intValue());
        
        // Total payout pending approval
        BigDecimal totalPayoutPendingApproval = commissionSummaryRepository.calculateTotalPendingCommission();
        dashboardData.setTotalPayoutPendingApproval(totalPayoutPendingApproval);
        
        // Top 5 earning staff
        List<Object[]> topStaffData = commissionSummaryRepository.findTopEarningStaffInMonth(
            currentMonth, PageRequest.of(0, 5)
        );
        
        List<CommissionReportDTO.TopEarningStaff> topEarningStaff = new ArrayList<>();
        for (Object[] data : topStaffData) {
            Long staffId = (Long) data[0];
            String firstName = (String) data[1];
            String lastName = (String) data[2];
            String branchName = (String) data[3];
            BigDecimal totalEarnings = (BigDecimal) data[4];
            Long totalServices = (Long) data[5];
            
            CommissionReportDTO.TopEarningStaff staff = new CommissionReportDTO.TopEarningStaff(
                staffId, firstName + " " + lastName, branchName, totalEarnings, totalServices.intValue()
            );
            topEarningStaff.add(staff);
        }
        dashboardData.setTopEarningStaff(topEarningStaff);
        
        // Branch comparisons
        List<Object[]> branchData = commissionSummaryRepository.findBranchWiseCommissionInMonth(currentMonth);
        List<CommissionReportDTO.BranchComparison> branchComparisons = new ArrayList<>();
        for (Object[] data : branchData) {
            Long branchId = (Long) data[0];
            String branchName = (String) data[1];
            BigDecimal totalCommission = (BigDecimal) data[2];
            Long totalServices = (Long) data[3];
            
            CommissionReportDTO.BranchComparison comparison = new CommissionReportDTO.BranchComparison(
                branchId, branchName, totalCommission, totalServices.intValue()
            );
            branchComparisons.add(comparison);
        }
        dashboardData.setBranchComparisons(branchComparisons);
        
        report.setDashboardData(dashboardData);
        return report;
    }
    
    /**
     * Generate monthly commission trend report
     */
    public CommissionReportDTO generateMonthlyTrendReport(int year) {
        CommissionReportDTO report = new CommissionReportDTO("MONTHLY_TREND", String.valueOf(year));
        
        List<CommissionReportDTO.BranchCommissionSummary> monthlyTrends = new ArrayList<>();
        
        // Get data for each month of the year
        for (int month = 1; month <= 12; month++) {
            String monthString = String.format("%d-%02d", year, month);
            
            // Get total commission for the month
            BigDecimal totalCommission = commissionSummaryRepository.calculateTotalCommissionForMonth(monthString);
            
            // Get total services for the month
            Long totalServices = commissionRepository.countServicesForBranchInMonth(
                null, year, month
            );
            
            CommissionReportDTO.BranchCommissionSummary monthlyData = new CommissionReportDTO.BranchCommissionSummary(
                null, "All Branches", monthString, totalServices.intValue(), totalCommission
            );
            monthlyTrends.add(monthlyData);
        }
        
        report.setBranchSummaries(monthlyTrends);
        return report;
    }
    
    /**
     * Generate quarterly commission report
     */
    public CommissionReportDTO generateQuarterlyReport(String year, int quarter) {
        String quarterString = String.format("%s-Q%d", year, quarter);
        CommissionReportDTO report = new CommissionReportDTO("QUARTERLY", quarterString);
        
        List<CommissionReportDTO.StaffCommissionSummary> quarterlyStaffData = new ArrayList<>();
        
        // Get data for each month in the quarter
        int startMonth = (quarter - 1) * 3 + 1;
        int endMonth = quarter * 3;
        
        for (int month = startMonth; month <= endMonth; month++) {
            String monthString = String.format("%s-%02d", year, month);
            
            // Get staff data for this month
            List<Object[]> staffData = commissionSummaryRepository.findTopEarningStaffInMonth(
                monthString, PageRequest.of(0, 1000)
            );
            
            for (Object[] data : staffData) {
                Long staffId = (Long) data[0];
                String firstName = (String) data[1];
                String lastName = (String) data[2];
                String branchName = (String) data[3];
                BigDecimal totalCommission = (BigDecimal) data[4];
                Long totalServices = (Long) data[5];
                
                CommissionReportDTO.StaffCommissionSummary summary = new CommissionReportDTO.StaffCommissionSummary(
                    staffId, firstName + " " + lastName, null,
                    null, branchName, monthString, totalServices.intValue(), totalCommission
                );
                quarterlyStaffData.add(summary);
            }
        }
        
        report.setStaffSummaries(quarterlyStaffData);
        return report;
    }
    
    /**
     * Generate year-end commission report
     */
    public CommissionReportDTO generateYearEndReport(String year) {
        CommissionReportDTO report = new CommissionReportDTO("YEAR_END", year);
        
        // Get all summaries for the year
        List<CommissionSummary> yearSummaries = commissionSummaryRepository.findByYear(year);
        
        // Group by staff
        List<CommissionReportDTO.StaffCommissionSummary> staffSummaries = new ArrayList<>();
        // Group by branch
        List<CommissionReportDTO.BranchCommissionSummary> branchSummaries = new ArrayList<>();
        
        // Process staff summaries
        yearSummaries.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                summary -> summary.getStaff().getId(),
                java.util.stream.Collectors.toList()
            ))
            .forEach((staffId, summaries) -> {
                CommissionSummary firstSummary = summaries.get(0);
                BigDecimal totalCommission = summaries.stream()
                    .map(CommissionSummary::getTotalCommission)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                Integer totalServices = summaries.stream()
                    .mapToInt(CommissionSummary::getTotalServices)
                    .sum();
                
                CommissionReportDTO.StaffCommissionSummary staffSummary = new CommissionReportDTO.StaffCommissionSummary(
                    staffId, firstSummary.getStaff().getFullName(), firstSummary.getStaff().getEmployeeCode(),
                    firstSummary.getBranch().getId(), firstSummary.getBranch().getBranchName(),
                    year, totalServices, totalCommission
                );
                staffSummaries.add(staffSummary);
            });
        
        // Process branch summaries
        yearSummaries.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                summary -> summary.getBranch().getId(),
                java.util.stream.Collectors.toList()
            ))
            .forEach((branchId, summaries) -> {
                CommissionSummary firstSummary = summaries.get(0);
                BigDecimal totalCommission = summaries.stream()
                    .map(CommissionSummary::getTotalCommission)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                Integer totalServices = summaries.stream()
                    .mapToInt(CommissionSummary::getTotalServices)
                    .sum();
                
                CommissionReportDTO.BranchCommissionSummary branchSummary = new CommissionReportDTO.BranchCommissionSummary(
                    branchId, firstSummary.getBranch().getBranchName(),
                    year, totalServices, totalCommission
                );
                branchSummaries.add(branchSummary);
            });
        
        report.setStaffSummaries(staffSummaries);
        report.setBranchSummaries(branchSummaries);
        
        return report;
    }
    
    /**
     * Get commission statistics for dashboard
     */
    public CommissionReportDTO.CommissionDashboardData getCommissionStatistics() {
        String currentMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        
        CommissionReportDTO.CommissionDashboardData dashboardData = new CommissionReportDTO.CommissionDashboardData();
        
        // Total commissions this month
        BigDecimal totalCommissionsThisMonth = commissionSummaryRepository.calculateTotalCommissionForMonth(currentMonth);
        dashboardData.setTotalCommissionsThisMonth(totalCommissionsThisMonth);
        
        // Total pending approvals
        Long totalPendingApprovals = commissionSummaryRepository.countPendingSummaries();
        dashboardData.setTotalPendingApprovals(totalPendingApprovals.intValue());
        
        // Total payout pending approval
        BigDecimal totalPayoutPendingApproval = commissionSummaryRepository.calculateTotalPendingCommission();
        dashboardData.setTotalPayoutPendingApproval(totalPayoutPendingApproval);
        
        return dashboardData;
    }
}
