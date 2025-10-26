package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.CommissionDTO;
import com.hexalyte.salon.dto.CommissionAdjustmentRequestDTO;
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
public class CommissionService {
    
    @Autowired
    private CommissionRepository commissionRepository;
    
    @Autowired
    private CommissionSummaryRepository commissionSummaryRepository;
    
    @Autowired
    private CommissionAdjustmentLogRepository adjustmentLogRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private BranchRepository branchRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuditLogger auditLogger;
    
    @Autowired
    private BillingItemRepository billingItemRepository;

    /**
     * Calculate and create commission for a completed appointment
     */
    public void calculateCommissionForAppointment(Long appointmentId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isEmpty() || appointmentOpt.get().getStatus() != Appointment.Status.COMPLETED) {
            throw new IllegalArgumentException("Appointment not found or not completed");
        }
        
        Appointment appointment = appointmentOpt.get();
        
        // Calculate commission for each service in the appointment
        for (com.hexalyte.salon.model.AppointmentService appointmentService : appointment.getAppointmentServices()) {
            calculateCommissionForService(appointment, appointmentService);
        }
        
        // Update commission summary
        updateCommissionSummary(appointment.getStaff().getId(), appointment.getBranch().getId());
    }
    
    /**
     * Calculate commission for a specific service
     */
    private void calculateCommissionForService(Appointment appointment, com.hexalyte.salon.model.AppointmentService appointmentService) {
        com.hexalyte.salon.model.Service service = appointmentService.getService();
        Staff staff = appointment.getStaff();
        Branch branch = appointment.getBranch();
        
        // Determine commission rate using hierarchy: Service > Branch > Staff
        CommissionRateInfo rateInfo = determineCommissionRate(service, staff, branch);
        
        // Calculate commission amount
        BigDecimal commissionAmount = calculateCommissionAmount(
            appointmentService.getPrice(), 
            rateInfo.getRate(), 
            rateInfo.getCommissionType()
        );
        
        // Create commission record
        Commission commission = new Commission();
        commission.setBranch(branch);
        commission.setStaff(staff);
        commission.setAppointment(appointment);
        commission.setService(service);
        commission.setCommissionType(rateInfo.getCommissionType());
        commission.setRate(rateInfo.getRate());
        commission.setAmount(commissionAmount);
        commission.setCalculatedOn(LocalDateTime.now());
        commission.setServicePrice(appointmentService.getPrice());
        commission.setCalculationRule(rateInfo.getRuleDescription());
        commission.setIsManual(false);
        commission.setStatus(Commission.CommissionStatus.PENDING);
        
        commissionRepository.save(commission);
        
        // Log the commission calculation
        auditLogger.logCommissionCalculation(commission);
    }
    
    /**
     * Convert Service CommissionType to Commission CommissionType
     */
    private Commission.CommissionType convertServiceCommissionType(com.hexalyte.salon.model.Service.CommissionType serviceType) {
        if (serviceType == null) {
            return Commission.CommissionType.PERCENT;
        }
        switch (serviceType) {
            case PERCENTAGE:
                return Commission.CommissionType.PERCENT;
            case FIXED_AMOUNT:
                return Commission.CommissionType.FIXED;
            default:
                return Commission.CommissionType.PERCENT;
        }
    }

    /**
     * Determine commission rate using hierarchy: Service > Branch > Staff
     */
    private CommissionRateInfo determineCommissionRate(com.hexalyte.salon.model.Service service, Staff staff, Branch branch) {
        // Check service-level commission first
        if (service.getCommissionRate() != null && service.getCommissionRate().compareTo(BigDecimal.ZERO) > 0) {
            return new CommissionRateInfo(
                service.getCommissionRate(),
                convertServiceCommissionType(service.getCommissionType()),
                "Service-level rate: " + service.getName()
            );
        }
        
        // Check branch-level commission (if implemented)
        // For now, we'll use staff-level commission
        if (staff.getCommissionRate() != null && staff.getCommissionRate().compareTo(BigDecimal.ZERO) > 0) {
            return new CommissionRateInfo(
                staff.getCommissionRate(),
                Commission.CommissionType.PERCENT,
                "Staff-level rate: " + staff.getFullName()
            );
        }
        
        // Default to 0% if no rate is set
        return new CommissionRateInfo(
            BigDecimal.ZERO,
            Commission.CommissionType.PERCENT,
            "No commission rate configured"
        );
    }
    
    /**
     * Calculate commission amount based on type and rate
     */
    private BigDecimal calculateCommissionAmount(BigDecimal servicePrice, BigDecimal rate, Commission.CommissionType type) {
        if (type == Commission.CommissionType.FIXED) {
            return rate; // Fixed amount
        } else {
            // Percentage-based calculation
            return servicePrice.multiply(rate).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
        }
    }
    
    /**
     * Update commission summary for staff and branch
     */
    private void updateCommissionSummary(Long staffId, Long branchId) {
        String currentMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        
        Optional<CommissionSummary> summaryOpt = commissionSummaryRepository
            .findByStaffIdAndBranchIdAndMonth(staffId, branchId, currentMonth);
        
        CommissionSummary summary;
        if (summaryOpt.isPresent()) {
            summary = summaryOpt.get();
        } else {
            Staff staff = staffRepository.findById(staffId).orElseThrow();
            Branch branch = branchRepository.findById(branchId).orElseThrow();
            summary = new CommissionSummary(staff, branch, currentMonth);
        }
        
        // Recalculate totals
        BigDecimal totalCommission = commissionRepository.calculateTotalCommissionForStaffInMonth(
            staffId, branchId, 
            LocalDateTime.now().getYear(), 
            LocalDateTime.now().getMonthValue()
        );
        
        Long totalServices = commissionRepository.countServicesForStaffInMonth(
            staffId, branchId,
            LocalDateTime.now().getYear(),
            LocalDateTime.now().getMonthValue()
        );
        
        summary.setTotalCommission(totalCommission);
        summary.setTotalServices(totalServices.intValue());
        
        commissionSummaryRepository.save(summary);
    }
    
    /**
     * Get all commissions with pagination and filtering
     */
    @Transactional(readOnly = true)
    public Page<CommissionDTO> getCommissions(Long staffId, Long branchId, 
                                            Commission.CommissionStatus status,
                                            LocalDateTime startDate, LocalDateTime endDate,
                                            Pageable pageable) {
        Page<Commission> commissions = commissionRepository.findByMultipleCriteria(
            staffId, branchId, status, startDate, endDate, pageable
        );
        
        return commissions.map(CommissionDTO::new);
    }
    
    /**
     * Get commission by ID
     */
    @Transactional(readOnly = true)
    public Optional<CommissionDTO> getCommissionById(Long id) {
        return commissionRepository.findById(id).map(CommissionDTO::new);
    }
    
    /**
     * Get commissions by staff
     */
    @Transactional(readOnly = true)
    public List<CommissionDTO> getCommissionsByStaff(Long staffId) {
        return commissionRepository.findByStaffId(staffId)
            .stream()
            .map(CommissionDTO::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get commissions by branch
     */
    @Transactional(readOnly = true)
    public List<CommissionDTO> getCommissionsByBranch(Long branchId) {
        return commissionRepository.findByBranchId(branchId)
            .stream()
            .map(CommissionDTO::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get pending commissions
     */
    @Transactional(readOnly = true)
    public List<CommissionDTO> getPendingCommissions() {
        return commissionRepository.findPendingApprovals()
            .stream()
            .map(CommissionDTO::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Approve commission
     */
    public void approveCommission(Long commissionId, Long approverId) {
        Commission commission = commissionRepository.findById(commissionId)
            .orElseThrow(() -> new IllegalArgumentException("Commission not found"));
        
        if (commission.isLocked()) {
            throw new IllegalStateException("Commission is locked and cannot be modified");
        }
        
        User approver = userRepository.findById(approverId)
            .orElseThrow(() -> new IllegalArgumentException("Approver not found"));
        
        commission.approve(approver);
        commissionRepository.save(commission);
        
        // Log the approval
        auditLogger.logCommissionApproval(commission, approver);
    }
    
    /**
     * Lock commission (for payroll processing)
     */
    public void lockCommission(Long commissionId) {
        Commission commission = commissionRepository.findById(commissionId)
            .orElseThrow(() -> new IllegalArgumentException("Commission not found"));
        
        commission.lock();
        commissionRepository.save(commission);
        
        // Log the lock
        auditLogger.logCommissionLock(commission);
    }
    
    /**
     * Reverse commission (for cancellations/refunds)
     */
    public void reverseCommission(Long commissionId) {
        Commission commission = commissionRepository.findById(commissionId)
            .orElseThrow(() -> new IllegalArgumentException("Commission not found"));
        
        if (commission.isLocked()) {
            throw new IllegalStateException("Commission is locked and cannot be modified");
        }
        
        commission.reverse();
        commissionRepository.save(commission);
        
        // Update commission summary
        updateCommissionSummary(commission.getStaff().getId(), commission.getBranch().getId());
        
        // Log the reversal
        auditLogger.logCommissionReversal(commission);
    }
    
    /**
     * Adjust commission amount
     */
    public void adjustCommission(CommissionAdjustmentRequestDTO request, Long userId) {
        Commission commission = commissionRepository.findById(request.getCommissionId())
            .orElseThrow(() -> new IllegalArgumentException("Commission not found"));
        
        if (commission.isLocked()) {
            throw new IllegalStateException("Commission is locked and cannot be modified");
        }
        
        BigDecimal oldAmount = commission.getAmount();
        BigDecimal newAmount = request.getNewAmount();
        
        // Create adjustment log
        CommissionAdjustmentLog adjustmentLog = new CommissionAdjustmentLog(
            commission,
            oldAmount,
            newAmount,
            request.getReason(),
            userId,
            CommissionAdjustmentLog.AdjustmentType.valueOf(request.getAdjustmentType())
        );
        
        // Update commission
        commission.setAmount(newAmount);
        commission.setIsManual(true);
        commissionRepository.save(commission);
        
        // Save adjustment log
        adjustmentLogRepository.save(adjustmentLog);
        
        // Update commission summary
        updateCommissionSummary(commission.getStaff().getId(), commission.getBranch().getId());
        
        // Log the adjustment
        auditLogger.logCommissionAdjustment(commission, adjustmentLog);
    }
    
    /**
     * Get commission adjustment history
     */
    @Transactional(readOnly = true)
    public List<CommissionAdjustmentLog> getCommissionAdjustmentHistory(Long commissionId) {
        return adjustmentLogRepository.findCommissionAdjustmentHistory(commissionId);
    }
    
    /**
     * Get total pending commission amount
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalPendingCommission() {
        return commissionRepository.calculateTotalPendingCommission();
    }
    
    /**
     * Get total approved commission for current month
     */
    @Transactional(readOnly = true)
    public BigDecimal getTotalApprovedCommissionThisMonth() {
        LocalDateTime now = LocalDateTime.now();
        return commissionRepository.calculateTotalApprovedCommissionInMonth(now.getYear(), now.getMonthValue());
    }
    
    // Helper class for commission rate information
    private static class CommissionRateInfo {
        private final BigDecimal rate;
        private final Commission.CommissionType commissionType;
        private final String ruleDescription;
        
        public CommissionRateInfo(BigDecimal rate, Commission.CommissionType commissionType, String ruleDescription) {
            this.rate = rate;
            this.commissionType = commissionType;
            this.ruleDescription = ruleDescription;
        }
        
        public BigDecimal getRate() { return rate; }
        public Commission.CommissionType getCommissionType() { return commissionType; }
        public String getRuleDescription() { return ruleDescription; }
    }
    
    /**
     * Generate commission from billing item
     */
    public void generateCommissionFromBillingItem(BillingItem billingItem) {
        if (billingItem.getCommissionGenerated()) {
            return; // Already generated
        }
        
        BigDecimal commissionAmount = billingItem.calculateCommission();
        if (commissionAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return; // No commission to generate
        }
        
        Commission commission = new Commission();
        commission.setStaff(billingItem.getStaff());
        commission.setAppointment(billingItem.getBilling().getAppointment());
        commission.setService(billingItem.getService());
        commission.setBranch(billingItem.getBilling().getBranch());
        commission.setAmount(commissionAmount);
        commission.setCommissionRate(billingItem.getService().getCommissionRate());
        commission.setCommissionDate(billingItem.getBilling().getBillDate());
        commission.setStatus(Commission.CommissionStatus.PENDING);
        
        commissionRepository.save(commission);
        
        // Update commission summary
        updateCommissionSummary(billingItem.getStaff().getId(), billingItem.getBilling().getBranch().getId());
        
        // Log the commission generation
        auditLogger.logCommissionCalculation(commission);
    }
    
    /**
     * Reverse commission from billing item (for refunds)
     */
    public void reverseCommissionFromBillingItem(BillingItem billingItem) {
        if (!billingItem.getCommissionGenerated()) {
            return; // No commission to reverse
        }
        
        // Find the commission for this billing item
        List<Commission> commissions = commissionRepository.findByAppointmentIdAndServiceIdAndStaffId(
            billingItem.getBilling().getAppointment().getId(),
            billingItem.getService().getId(),
            billingItem.getStaff().getId()
        );
        
        for (Commission commission : commissions) {
            if (commission.getStatus() != Commission.CommissionStatus.REVERSED) {
                reverseCommission(commission.getId());
            }
        }
        
        // Update commission summary
        updateCommissionSummary(billingItem.getStaff().getId(), billingItem.getBilling().getBranch().getId());
    }
}