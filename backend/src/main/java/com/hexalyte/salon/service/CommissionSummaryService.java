package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.CommissionSummaryDTO;
import com.hexalyte.salon.model.*;
import com.hexalyte.salon.repository.*;
import com.hexalyte.salon.security.AuditLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommissionSummaryService {
    
    @Autowired
    private CommissionSummaryRepository commissionSummaryRepository;
    
    @Autowired
    private CommissionRepository commissionRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private BranchRepository branchRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuditLogger auditLogger;

    /**
     * Generate commission summaries for a specific month
     */
    public void generateCommissionSummariesForMonth(String month) {
        // Parse year and month
        String[] parts = month.split("-");
        int year = Integer.parseInt(parts[0]);
        int monthValue = Integer.parseInt(parts[1]);
        
        // Find all staff-branch combinations that have commissions for this month
        List<Object[]> staffBranchCombinations = commissionRepository
            .findStaffBranchCombinationsForMonth(year, monthValue);
        
        for (Object[] combination : staffBranchCombinations) {
            Long staffId = (Long) combination[0];
            Long branchId = (Long) combination[1];
            
            generateCommissionSummaryForStaffBranch(staffId, branchId, month);
        }
    }
    
    /**
     * Generate commission summary for a specific staff-branch combination
     */
    private void generateCommissionSummaryForStaffBranch(Long staffId, Long branchId, String month) {
        // Check if summary already exists
        if (commissionSummaryRepository.existsByStaffIdAndBranchIdAndMonth(staffId, branchId, month)) {
            return; // Summary already exists
        }
        
        Staff staff = staffRepository.findById(staffId)
            .orElseThrow(() -> new IllegalArgumentException("Staff not found"));
        
        Branch branch = branchRepository.findById(branchId)
            .orElseThrow(() -> new IllegalArgumentException("Branch not found"));
        
        // Parse month for calculations
        String[] parts = month.split("-");
        int year = Integer.parseInt(parts[0]);
        int monthValue = Integer.parseInt(parts[1]);
        
        // Calculate totals
        BigDecimal totalCommission = commissionRepository.calculateTotalCommissionForStaffInMonth(
            staffId, branchId, year, monthValue
        );
        
        Long totalServices = commissionRepository.countServicesForStaffInMonth(
            staffId, branchId, year, monthValue
        );
        
        // Create summary
        CommissionSummary summary = new CommissionSummary(staff, branch, month);
        summary.setTotalCommission(totalCommission);
        summary.setTotalServices(totalServices.intValue());
        summary.setStatus(CommissionSummary.SummaryStatus.PENDING);
        
        commissionSummaryRepository.save(summary);
        
        // Log the generation
        auditLogger.logCommissionSummaryGeneration(summary);
    }
    
    /**
     * Get commission summaries with pagination and filtering
     */
    @Transactional(readOnly = true)
    public Page<CommissionSummaryDTO> getCommissionSummaries(Long staffId, Long branchId,
                                                           CommissionSummary.SummaryStatus status,
                                                           String month, Pageable pageable) {
        Page<CommissionSummary> summaries = commissionSummaryRepository.findByMultipleCriteria(
            staffId, branchId, status, month, pageable
        );
        
        return summaries.map(CommissionSummaryDTO::new);
    }
    
    /**
     * Get commission summary by ID
     */
    @Transactional(readOnly = true)
    public Optional<CommissionSummaryDTO> getCommissionSummaryById(Long id) {
        return commissionSummaryRepository.findById(id).map(CommissionSummaryDTO::new);
    }
    
    /**
     * Get commission summaries by staff
     */
    @Transactional(readOnly = true)
    public List<CommissionSummaryDTO> getCommissionSummariesByStaff(Long staffId) {
        return commissionSummaryRepository.findByStaffId(staffId)
            .stream()
            .map(CommissionSummaryDTO::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get commission summaries by branch
     */
    @Transactional(readOnly = true)
    public List<CommissionSummaryDTO> getCommissionSummariesByBranch(Long branchId) {
        return commissionSummaryRepository.findByBranchId(branchId)
            .stream()
            .map(CommissionSummaryDTO::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get commission summaries by month
     */
    @Transactional(readOnly = true)
    public List<CommissionSummaryDTO> getCommissionSummariesByMonth(String month) {
        return commissionSummaryRepository.findByMonth(month)
            .stream()
            .map(CommissionSummaryDTO::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get pending commission summaries
     */
    @Transactional(readOnly = true)
    public List<CommissionSummaryDTO> getPendingSummaries() {
        return commissionSummaryRepository.findPendingSummaries()
            .stream()
            .map(CommissionSummaryDTO::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get approved commission summaries
     */
    @Transactional(readOnly = true)
    public List<CommissionSummaryDTO> getApprovedSummaries() {
        return commissionSummaryRepository.findApprovedSummaries()
            .stream()
            .map(CommissionSummaryDTO::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get locked commission summaries
     */
    @Transactional(readOnly = true)
    public List<CommissionSummaryDTO> getLockedSummaries() {
        return commissionSummaryRepository.findLockedSummaries()
            .stream()
            .map(CommissionSummaryDTO::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Approve commission summary
     */
    public void approveCommissionSummary(Long summaryId, Long approverId) {
        CommissionSummary summary = commissionSummaryRepository.findById(summaryId)
            .orElseThrow(() -> new IllegalArgumentException("Commission summary not found"));
        
        if (summary.isLocked()) {
            throw new IllegalStateException("Commission summary is locked and cannot be modified");
        }
        
        summary.approve(approverId);
        commissionSummaryRepository.save(summary);
        
        // Log the approval
        auditLogger.logCommissionSummaryApproval(summary, approverId);
    }
    
    /**
     * Lock commission summary (for payroll processing)
     */
    public void lockCommissionSummary(Long summaryId) {
        CommissionSummary summary = commissionSummaryRepository.findById(summaryId)
            .orElseThrow(() -> new IllegalArgumentException("Commission summary not found"));
        
        summary.lock();
        commissionSummaryRepository.save(summary);
        
        // Log the lock
        auditLogger.logCommissionSummaryLock(summary);
    }
    
    /**
     * Approve all pending summaries for a month
     */
    public void approveAllSummariesForMonth(String month, Long approverId) {
        List<CommissionSummary> summaries = commissionSummaryRepository.findByMonthAndStatus(
            month, CommissionSummary.SummaryStatus.PENDING
        );
        
        for (CommissionSummary summary : summaries) {
            summary.approve(approverId);
            commissionSummaryRepository.save(summary);
            
            // Log the approval
            auditLogger.logCommissionSummaryApproval(summary, approverId);
        }
    }
    
    /**
     * Lock all approved summaries for a month
     */
    public void lockAllSummariesForMonth(String month) {
        List<CommissionSummary> summaries = commissionSummaryRepository.findByMonthAndStatus(
            month, CommissionSummary.SummaryStatus.APPROVED
        );
        
        for (CommissionSummary summary : summaries) {
            summary.lock();
            commissionSummaryRepository.save(summary);
            
            // Log the lock
            auditLogger.logCommissionSummaryLock(summary);
        }
    }
    
    /**
     * Get total commission for a month
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalCommissionForMonth(String month) {
        return commissionSummaryRepository.calculateTotalCommissionForMonth(month);
    }
    
    /**
     * Get total commission for branch in a month
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalCommissionForBranchInMonth(Long branchId, String month) {
        return commissionSummaryRepository.calculateTotalCommissionForBranchInMonth(branchId, month);
    }
    
    /**
     * Get total commission for staff in a month
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalCommissionForStaffInMonth(Long staffId, String month) {
        return commissionSummaryRepository.calculateTotalCommissionForStaffInMonth(staffId, month);
    }
    
    /**
     * Get count of pending summaries
     */
    @Transactional(readOnly = true)
    public Long getPendingSummariesCount() {
        return commissionSummaryRepository.countPendingSummaries();
    }
    
    /**
     * Get total pending commission amount
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalPendingCommission() {
        return commissionSummaryRepository.calculateTotalPendingCommission();
    }
    
    /**
     * Regenerate commission summary for a specific staff-branch-month
     */
    public void regenerateCommissionSummary(Long staffId, Long branchId, String month) {
        // Delete existing summary if it exists
        Optional<CommissionSummary> existingSummary = commissionSummaryRepository
            .findByStaffIdAndBranchIdAndMonth(staffId, branchId, month);
        
        if (existingSummary.isPresent()) {
            commissionSummaryRepository.delete(existingSummary.get());
        }
        
        // Generate new summary
        generateCommissionSummaryForStaffBranch(staffId, branchId, month);
    }
    
    /**
     * Generate summaries for current month
     */
    public void generateCurrentMonthSummaries() {
        String currentMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        generateCommissionSummariesForMonth(currentMonth);
    }
    
    /**
     * Get commission summaries by year
     */
    @Transactional(readOnly = true)
    public List<CommissionSummaryDTO> getCommissionSummariesByYear(String year) {
        return commissionSummaryRepository.findByYear(year)
            .stream()
            .map(CommissionSummaryDTO::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get commission summaries by quarter
     */
    @Transactional(readOnly = true)
    public List<CommissionSummaryDTO> getCommissionSummariesByQuarter(String year, int quarter) {
        // This would need to be implemented in the repository
        // For now, return empty list
        return List.of();
    }
}
