package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.*;
import com.hexalyte.salon.model.Commission;
import com.hexalyte.salon.model.CommissionAdjustmentLog;
import com.hexalyte.salon.model.CommissionSummary;
import com.hexalyte.salon.service.*;
import com.hexalyte.salon.security.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/commissions")
@CrossOrigin(origins = "*")
public class CommissionController {
    
    @Autowired
    private CommissionService commissionService;
    
    @Autowired
    private CommissionSummaryService commissionSummaryService;
    
    @Autowired
    private CommissionReportService commissionReportService;
    
    @Autowired
    private AuthUtils authUtils;

    // Commission Management Endpoints

    /**
     * Get all commissions with pagination and filtering
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<Page<CommissionDTO>> getCommissions(
            @RequestParam(required = false) Long staffId,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        
        Commission.CommissionStatus commissionStatus = null;
        if (status != null) {
            try {
                commissionStatus = Commission.CommissionStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        
        Page<CommissionDTO> commissions = commissionService.getCommissions(
            staffId, branchId, commissionStatus, startDate, endDate, pageable
        );
        
        return ResponseEntity.ok(commissions);
    }

    /**
     * Get commission by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<CommissionDTO> getCommissionById(@PathVariable Long id) {
        Optional<CommissionDTO> commission = commissionService.getCommissionById(id);
        return commission.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get commissions by staff
     */
    @GetMapping("/staff/{staffId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT') or (hasRole('STAFF') and @authUtils.isCurrentUserStaff(#staffId))")
    public ResponseEntity<List<CommissionDTO>> getCommissionsByStaff(@PathVariable Long staffId) {
        List<CommissionDTO> commissions = commissionService.getCommissionsByStaff(staffId);
        return ResponseEntity.ok(commissions);
    }

    /**
     * Get commissions by branch
     */
    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<List<CommissionDTO>> getCommissionsByBranch(@PathVariable Long branchId) {
        List<CommissionDTO> commissions = commissionService.getCommissionsByBranch(branchId);
        return ResponseEntity.ok(commissions);
    }

    /**
     * Get pending commissions
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<List<CommissionDTO>> getPendingCommissions() {
        List<CommissionDTO> commissions = commissionService.getPendingCommissions();
        return ResponseEntity.ok(commissions);
    }

    /**
     * Approve commission
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> approveCommission(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long approverId = authUtils.getCurrentUserId(request);
            commissionService.approveCommission(id, approverId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Lock commission
     */
    @PostMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<Void> lockCommission(@PathVariable Long id) {
        try {
            commissionService.lockCommission(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Reverse commission
     */
    @PostMapping("/{id}/reverse")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> reverseCommission(@PathVariable Long id) {
        try {
            commissionService.reverseCommission(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Adjust commission amount
     */
    @PostMapping("/adjust")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> adjustCommission(@Valid @RequestBody CommissionAdjustmentRequestDTO request, 
                                               HttpServletRequest httpRequest) {
        try {
            Long userId = authUtils.getCurrentUserId(httpRequest);
            commissionService.adjustCommission(request, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get commission adjustment history
     */
    @GetMapping("/{id}/adjustments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<List<CommissionAdjustmentLog>> getCommissionAdjustmentHistory(@PathVariable Long id) {
        List<CommissionAdjustmentLog> adjustments = commissionService.getCommissionAdjustmentHistory(id);
        return ResponseEntity.ok(adjustments);
    }

    // Commission Summary Endpoints

    /**
     * Get commission summaries with pagination and filtering
     */
    @GetMapping("/summaries")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<Page<CommissionSummaryDTO>> getCommissionSummaries(
            @RequestParam(required = false) Long staffId,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String month,
            Pageable pageable) {
        
        CommissionSummary.SummaryStatus summaryStatus = null;
        if (status != null) {
            try {
                summaryStatus = CommissionSummary.SummaryStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        
        Page<CommissionSummaryDTO> summaries = commissionSummaryService.getCommissionSummaries(
            staffId, branchId, summaryStatus, month, pageable
        );
        
        return ResponseEntity.ok(summaries);
    }

    /**
     * Get commission summary by ID
     */
    @GetMapping("/summaries/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<CommissionSummaryDTO> getCommissionSummaryById(@PathVariable Long id) {
        Optional<CommissionSummaryDTO> summary = commissionSummaryService.getCommissionSummaryById(id);
        return summary.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get commission summaries by staff
     */
    @GetMapping("/summaries/staff/{staffId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT') or (hasRole('STAFF') and @authUtils.isCurrentUserStaff(#staffId))")
    public ResponseEntity<List<CommissionSummaryDTO>> getCommissionSummariesByStaff(@PathVariable Long staffId) {
        List<CommissionSummaryDTO> summaries = commissionSummaryService.getCommissionSummariesByStaff(staffId);
        return ResponseEntity.ok(summaries);
    }

    /**
     * Get commission summaries by branch
     */
    @GetMapping("/summaries/branch/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<List<CommissionSummaryDTO>> getCommissionSummariesByBranch(@PathVariable Long branchId) {
        List<CommissionSummaryDTO> summaries = commissionSummaryService.getCommissionSummariesByBranch(branchId);
        return ResponseEntity.ok(summaries);
    }

    /**
     * Get commission summaries by month
     */
    @GetMapping("/summaries/month/{month}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<List<CommissionSummaryDTO>> getCommissionSummariesByMonth(@PathVariable String month) {
        List<CommissionSummaryDTO> summaries = commissionSummaryService.getCommissionSummariesByMonth(month);
        return ResponseEntity.ok(summaries);
    }

    /**
     * Get pending commission summaries
     */
    @GetMapping("/summaries/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<List<CommissionSummaryDTO>> getPendingSummaries() {
        List<CommissionSummaryDTO> summaries = commissionSummaryService.getPendingSummaries();
        return ResponseEntity.ok(summaries);
    }

    /**
     * Approve commission summary
     */
    @PostMapping("/summaries/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> approveCommissionSummary(@PathVariable Long id, HttpServletRequest request) {
        try {
            Long approverId = authUtils.getCurrentUserId(request);
            commissionSummaryService.approveCommissionSummary(id, approverId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Lock commission summary
     */
    @PostMapping("/summaries/{id}/lock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<Void> lockCommissionSummary(@PathVariable Long id) {
        try {
            commissionSummaryService.lockCommissionSummary(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Approve all summaries for a month
     */
    @PostMapping("/summaries/approve-month")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> approveAllSummariesForMonth(@RequestParam String month, HttpServletRequest request) {
        try {
            Long approverId = authUtils.getCurrentUserId(request);
            commissionSummaryService.approveAllSummariesForMonth(month, approverId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Lock all summaries for a month
     */
    @PostMapping("/summaries/lock-month")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<Void> lockAllSummariesForMonth(@RequestParam String month) {
        try {
            commissionSummaryService.lockAllSummariesForMonth(month);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Generate commission summaries for a month
     */
    @PostMapping("/summaries/generate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> generateCommissionSummaries(@RequestParam String month) {
        try {
            commissionSummaryService.generateCommissionSummariesForMonth(month);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Generate current month summaries
     */
    @PostMapping("/summaries/generate-current")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> generateCurrentMonthSummaries() {
        try {
            commissionSummaryService.generateCurrentMonthSummaries();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Commission Report Endpoints

    /**
     * Generate branch-wise commission report
     */
    @GetMapping("/reports/branch-wise")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<CommissionReportDTO> generateBranchWiseReport(@RequestParam String month) {
        CommissionReportDTO report = commissionReportService.generateBranchWiseReport(month);
        return ResponseEntity.ok(report);
    }

    /**
     * Generate staff-wise commission report
     */
    @GetMapping("/reports/staff-wise")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<CommissionReportDTO> generateStaffWiseReport(@RequestParam String month) {
        CommissionReportDTO report = commissionReportService.generateStaffWiseReport(month);
        return ResponseEntity.ok(report);
    }

    /**
     * Generate commission dashboard report
     */
    @GetMapping("/reports/dashboard")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<CommissionReportDTO> generateDashboardReport() {
        CommissionReportDTO report = commissionReportService.generateDashboardReport();
        return ResponseEntity.ok(report);
    }

    /**
     * Generate monthly trend report
     */
    @GetMapping("/reports/monthly-trend")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<CommissionReportDTO> generateMonthlyTrendReport(@RequestParam int year) {
        CommissionReportDTO report = commissionReportService.generateMonthlyTrendReport(year);
        return ResponseEntity.ok(report);
    }

    /**
     * Generate quarterly report
     */
    @GetMapping("/reports/quarterly")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<CommissionReportDTO> generateQuarterlyReport(@RequestParam String year, @RequestParam int quarter) {
        CommissionReportDTO report = commissionReportService.generateQuarterlyReport(year, quarter);
        return ResponseEntity.ok(report);
    }

    /**
     * Generate year-end report
     */
    @GetMapping("/reports/year-end")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<CommissionReportDTO> generateYearEndReport(@RequestParam String year) {
        CommissionReportDTO report = commissionReportService.generateYearEndReport(year);
        return ResponseEntity.ok(report);
    }

    // Statistics Endpoints

    /**
     * Get total pending commission amount
     */
    @GetMapping("/statistics/pending-total")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<BigDecimal> getTotalPendingCommission() {
        BigDecimal total = commissionService.getTotalPendingCommission();
        return ResponseEntity.ok(total);
    }

    /**
     * Get total approved commission for current month
     */
    @GetMapping("/statistics/approved-current-month")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<BigDecimal> getTotalApprovedCommissionThisMonth() {
        BigDecimal total = commissionService.getTotalApprovedCommissionThisMonth();
        return ResponseEntity.ok(total);
    }

    /**
     * Get commission statistics for dashboard
     */
    @GetMapping("/statistics/dashboard")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<CommissionReportDTO.CommissionDashboardData> getCommissionStatistics() {
        CommissionReportDTO.CommissionDashboardData statistics = commissionReportService.getCommissionStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Get pending summaries count
     */
    @GetMapping("/statistics/pending-count")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('ACCOUNTANT')")
    public ResponseEntity<Long> getPendingSummariesCount() {
        Long count = commissionSummaryService.getPendingSummariesCount();
        return ResponseEntity.ok(count);
    }
}
