package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.*;
import com.hexalyte.salon.model.*;
import com.hexalyte.salon.repository.*;
import com.hexalyte.salon.security.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillingService {
    
    @Autowired
    private BillingRepository billingRepository;
    
    @Autowired
    private BillingItemRepository billingItemRepository;
    
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private BranchRepository branchRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private CommissionService commissionService;
    
    @Autowired
    private AuthUtils authUtils;
    
    // System settings for tax and service charge
    private static final BigDecimal DEFAULT_TAX_RATE = new BigDecimal("15.0"); // 15%
    private static final BigDecimal DEFAULT_SERVICE_CHARGE = new BigDecimal("5.0"); // 5%
    private static final BigDecimal LOYALTY_POINTS_VALUE = new BigDecimal("1.0"); // 1 point = 1 rupee
    
    /**
     * Generate bill automatically when appointment is completed
     */
    public BillingDTO generateBillFromAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        if (appointment.getStatus() != Appointment.Status.COMPLETED) {
            throw new RuntimeException("Appointment must be completed to generate bill");
        }
        
        // Check if bill already exists
        if (billingRepository.existsByAppointmentId(appointmentId)) {
            throw new RuntimeException("Bill already exists for this appointment");
        }
        
        // Generate bill number
        String billNumber = generateBillNumber(appointment.getBranch().getId());
        
        // Calculate subtotal from appointment services
        BigDecimal subtotal = calculateSubtotalFromAppointment(appointment);
        
        // Calculate tax
        BigDecimal taxAmount = calculateTax(subtotal);
        
        // Calculate total
        BigDecimal totalAmount = subtotal.add(taxAmount);
        
        // Create billing entity
        Billing billing = new Billing(billNumber, appointment.getBranch(), appointment, 
                                    appointment.getCustomer(), subtotal, totalAmount);
        billing.setTaxAmount(taxAmount);
        billing.setCreatedBy(authUtils.getCurrentUser());
        billing.setBillDate(LocalDateTime.now());
        
        // Save billing
        billing = billingRepository.save(billing);
        
        // Create billing items from appointment services
        createBillingItemsFromAppointment(billing, appointment);
        
        // Generate commissions
        generateCommissionsForBilling(billing);
        
        return new BillingDTO(billing);
    }
    
    /**
     * Process payment for a bill
     */
    public BillingDTO processPayment(Long billId, BillingRequestDTO billingRequest) {
        Billing billing = billingRepository.findById(billId)
            .orElseThrow(() -> new RuntimeException("Bill not found"));
        
        // Update discount and loyalty if provided
        if (billingRequest.getDiscountAmount() != null) {
            billing.setDiscountAmount(billingRequest.getDiscountAmount());
        }
        if (billingRequest.getLoyaltyRedeemed() != null) {
            billing.setLoyaltyRedeemed(billingRequest.getLoyaltyRedeemed());
        }
        if (billingRequest.getRemarks() != null) {
            billing.setRemarks(billingRequest.getRemarks());
        }
        
        // Recalculate total with discounts
        recalculateBillTotal(billing);
        
        // Process payments
        BigDecimal totalPaid = BigDecimal.ZERO;
        for (PaymentRequestDTO paymentRequest : billingRequest.getPayments()) {
            PaymentTransaction transaction = processPaymentTransaction(billing, paymentRequest);
            totalPaid = totalPaid.add(transaction.getAmount());
        }
        
        // Update billing payment status
        billing.addPayment(totalPaid);
        billing.setPaymentMethod(determinePaymentMethod(billingRequest.getPayments()));
        
        billing = billingRepository.save(billing);
        
        return new BillingDTO(billing);
    }
    
    /**
     * Get bill by ID
     */
    public BillingDTO getBillById(Long billId) {
        Billing billing = billingRepository.findById(billId)
            .orElseThrow(() -> new RuntimeException("Bill not found"));
        return new BillingDTO(billing);
    }
    
    /**
     * Get bills by branch with pagination
     */
    public Page<BillingDTO> getBillsByBranch(Long branchId, Pageable pageable) {
        Page<Billing> bills = billingRepository.findByBranchIdOrderByBillDateDesc(branchId, pageable);
        return bills.map(BillingDTO::new);
    }
    
    /**
     * Get bills by customer
     */
    public List<BillingDTO> getBillsByCustomer(Long customerId) {
        List<Billing> bills = billingRepository.findByCustomerIdOrderByBillDateDesc(customerId);
        return bills.stream().map(BillingDTO::new).collect(Collectors.toList());
    }
    
    /**
     * Get unpaid bills
     */
    public List<BillingDTO> getUnpaidBills(Long branchId) {
        List<Billing> bills = billingRepository.findUnpaidBillsByBranch(branchId);
        return bills.stream().map(BillingDTO::new).collect(Collectors.toList());
    }
    
    /**
     * Get billing report
     */
    public BillingReportDTO getBillingReport(Long branchId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        
        BillingReportDTO report = new BillingReportDTO();
        report.setBranchId(branchId);
        report.setBranchName(branchRepository.findById(branchId).orElseThrow().getBranchName());
        report.setReportDate(LocalDate.now());
        report.setGeneratedAt(LocalDateTime.now());
        
        // Get summary totals
        BigDecimal totalRevenue = billingRepository.getRevenueByBranchAndDateRange(branchId, startDateTime, endDateTime);
        BigDecimal totalTax = billingRepository.getTaxCollectedByBranchAndDateRange(branchId, startDateTime, endDateTime);
        BigDecimal totalDiscount = billingRepository.getDiscountGivenByBranchAndDateRange(branchId, startDateTime, endDateTime);
        BigDecimal totalLoyalty = billingRepository.getLoyaltyRedeemedByBranchAndDateRange(branchId, startDateTime, endDateTime);
        
        report.setTotalRevenue(totalRevenue);
        report.setTotalTax(totalTax);
        report.setTotalDiscount(totalDiscount);
        report.setTotalLoyaltyRedeemed(totalLoyalty);
        
        // Get bill counts
        Long totalBills = billingRepository.countByBranchAndDateRange(branchId, startDateTime, endDateTime);
        Long paidBills = billingRepository.countByBranchAndStatus(branchId, Billing.BillingStatus.PAID);
        Long partialBills = billingRepository.countByBranchAndStatus(branchId, Billing.BillingStatus.PARTIAL);
        Long unpaidBills = billingRepository.countByBranchAndStatus(branchId, Billing.BillingStatus.UNPAID);
        
        report.setTotalBills(totalBills.intValue());
        report.setPaidBills(paidBills.intValue());
        report.setPartialBills(partialBills.intValue());
        report.setUnpaidBills(unpaidBills.intValue());
        
        // Get payment method breakdown
        List<Object[]> paymentMethodData = billingRepository.getPaymentMethodBreakdownByBranchAndDateRange(branchId, startDateTime, endDateTime);
        List<PaymentMethodSummaryDTO> paymentMethodBreakdown = paymentMethodData.stream()
            .map(row -> new PaymentMethodSummaryDTO(
                (String) row[0], 
                (BigDecimal) row[1], 
                ((Number) row[2]).intValue()
            ))
            .collect(Collectors.toList());
        report.setPaymentMethodBreakdown(paymentMethodBreakdown);
        
        // Get daily revenue
        List<Object[]> dailyRevenueData = billingRepository.getDailyRevenueByBranchAndDateRange(branchId, startDateTime, endDateTime);
        List<DailyRevenueDTO> dailyRevenue = dailyRevenueData.stream()
            .map(row -> new DailyRevenueDTO(
                ((java.sql.Date) row[0]).toLocalDate(),
                (BigDecimal) row[1],
                ((Number) row[2]).intValue()
            ))
            .collect(Collectors.toList());
        report.setDailyRevenue(dailyRevenue);
        
        // Get top services
        List<Object[]> serviceRevenueData = billingItemRepository.getServiceRevenueByBranchAndDateRange(branchId, startDateTime, endDateTime);
        List<ServiceRevenueDTO> topServices = serviceRevenueData.stream()
            .map(row -> new ServiceRevenueDTO(
                ((Number) row[0]).longValue(),
                (String) row[1],
                (String) row[2],
                (BigDecimal) row[3],
                ((Number) row[4]).intValue()
            ))
            .collect(Collectors.toList());
        report.setTopServices(topServices);
        
        // Get staff performance
        List<Object[]> staffRevenueData = billingItemRepository.getStaffRevenueByBranchAndDateRange(branchId, startDateTime, endDateTime);
        List<StaffRevenueDTO> staffPerformance = staffRevenueData.stream()
            .map(row -> new StaffRevenueDTO(
                ((Number) row[0]).longValue(),
                (String) row[1] + " " + (String) row[2],
                (String) row[3],
                (String) row[4],
                (BigDecimal) row[5],
                ((Number) row[6]).intValue(),
                (BigDecimal) row[7]
            ))
            .collect(Collectors.toList());
        report.setStaffPerformance(staffPerformance);
        
        return report;
    }
    
    /**
     * Refund a bill
     */
    public BillingDTO refundBill(Long billId, String reason) {
        Billing billing = billingRepository.findById(billId)
            .orElseThrow(() -> new RuntimeException("Bill not found"));
        
        if (billing.getStatus() != Billing.BillingStatus.PAID) {
            throw new RuntimeException("Only paid bills can be refunded");
        }
        
        // Create refund transaction
        PaymentTransaction refundTransaction = new PaymentTransaction(
            billing, 
            "REFUND", 
            billing.getPaidAmount().negate(), 
            "REF-" + System.currentTimeMillis()
        );
        refundTransaction.setStatus(PaymentTransaction.TransactionStatus.REFUNDED);
        paymentTransactionRepository.save(refundTransaction);
        
        // Update billing status
        billing.setStatus(Billing.BillingStatus.REFUNDED);
        billing.setRemarks(billing.getRemarks() + "\nRefunded: " + reason);
        billing = billingRepository.save(billing);
        
        // Reverse commissions
        reverseCommissionsForBilling(billing);
        
        return new BillingDTO(billing);
    }
    
    // Private helper methods
    
    private String generateBillNumber(Long branchId) {
        Branch branch = branchRepository.findById(branchId).orElseThrow();
        String branchCode = branch.getBranchCode();
        String year = String.valueOf(LocalDate.now().getYear());
        String month = String.format("%02d", LocalDate.now().getMonthValue());
        
        // Get next sequence number for this branch and month
        String prefix = branchCode + "-" + year + "-" + month + "-";
        Long lastBillNumber = billingRepository.countByBranchIdAndBillNumberStartingWith(branchId, prefix);
        
        return prefix + String.format("%06d", lastBillNumber + 1);
    }
    
    private BigDecimal calculateSubtotalFromAppointment(Appointment appointment) {
        return appointment.getAppointmentServices().stream()
            .map(appointmentService -> appointmentService.getPrice().multiply(BigDecimal.valueOf(appointmentService.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private BigDecimal calculateTax(BigDecimal subtotal) {
        return subtotal.multiply(DEFAULT_TAX_RATE).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
    
    private void createBillingItemsFromAppointment(Billing billing, Appointment appointment) {
        for (com.hexalyte.salon.model.AppointmentService appointmentService : appointment.getAppointmentServices()) {
            BillingItem billingItem = new BillingItem(
                billing,
                appointmentService.getService(),
                appointment.getStaff(),
                appointmentService.getQuantity(),
                appointmentService.getPrice()
            );
            billingItemRepository.save(billingItem);
        }
    }
    
    private void generateCommissionsForBilling(Billing billing) {
        for (BillingItem billingItem : billing.getBillingItems()) {
            if (!billingItem.getCommissionGenerated()) {
                commissionService.generateCommissionFromBillingItem(billingItem);
                billingItem.markCommissionGenerated();
                billingItemRepository.save(billingItem);
            }
        }
    }
    
    private void reverseCommissionsForBilling(Billing billing) {
        for (BillingItem billingItem : billing.getBillingItems()) {
            if (billingItem.getCommissionGenerated()) {
                commissionService.reverseCommissionFromBillingItem(billingItem);
                billingItem.setCommissionGenerated(false);
                billingItemRepository.save(billingItem);
            }
        }
    }
    
    private PaymentTransaction processPaymentTransaction(Billing billing, PaymentRequestDTO paymentRequest) {
        PaymentTransaction transaction = new PaymentTransaction(
            billing,
            paymentRequest.getPaymentMethod(),
            paymentRequest.getAmount(),
            paymentRequest.getReferenceNo()
        );
        
        if (paymentRequest.getGatewayTransactionId() != null) {
            transaction.setGatewayTransactionId(paymentRequest.getGatewayTransactionId());
        }
        if (paymentRequest.getGatewayResponse() != null) {
            transaction.setGatewayResponse(paymentRequest.getGatewayResponse());
        }
        
        return paymentTransactionRepository.save(transaction);
    }
    
    private String determinePaymentMethod(List<PaymentRequestDTO> payments) {
        if (payments.size() == 1) {
            return payments.get(0).getPaymentMethod();
        } else {
            return "MIXED";
        }
    }
    
    private void recalculateBillTotal(Billing billing) {
        BigDecimal subtotal = billing.getSubtotal();
        BigDecimal discount = billing.getDiscountAmount() != null ? billing.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal loyalty = billing.getLoyaltyRedeemed() != null ? billing.getLoyaltyRedeemed() : BigDecimal.ZERO;
        BigDecimal taxableAmount = subtotal.subtract(discount).subtract(loyalty);
        BigDecimal tax = calculateTax(taxableAmount);
        BigDecimal total = taxableAmount.add(tax);
        
        billing.setTaxAmount(tax);
        billing.setTotalAmount(total);
        billing.setBalanceAmount(total.subtract(billing.getPaidAmount()));
    }
}
