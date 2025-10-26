package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.*;
import com.hexalyte.salon.service.BillingService;
import com.hexalyte.salon.service.InvoiceService;
import com.hexalyte.salon.security.RequireBranchAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/billing")
@Tag(name = "Billing Management", description = "APIs for billing and payment management")
public class BillingController {
    
    @Autowired
    private BillingService billingService;
    
    @Autowired
    private InvoiceService invoiceService;
    
    @PostMapping("/generate/{appointmentId}")
    @Operation(summary = "Generate bill from completed appointment", 
               description = "Automatically generates a bill when an appointment is completed")
    @RequireBranchAccess
    public ResponseEntity<BillingDTO> generateBill(
            @Parameter(description = "Appointment ID") @PathVariable Long appointmentId) {
        try {
            BillingDTO billing = billingService.generateBillFromAppointment(appointmentId);
            return ResponseEntity.ok(billing);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{billId}/payment")
    @Operation(summary = "Process payment for a bill", 
               description = "Process payment transactions for a bill including split payments")
    @RequireBranchAccess
    public ResponseEntity<BillingDTO> processPayment(
            @Parameter(description = "Bill ID") @PathVariable Long billId,
            @Valid @RequestBody BillingRequestDTO billingRequest) {
        try {
            BillingDTO billing = billingService.processPayment(billId, billingRequest);
            return ResponseEntity.ok(billing);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{billId}")
    @Operation(summary = "Get bill by ID", description = "Retrieve detailed bill information")
    @RequireBranchAccess
    public ResponseEntity<BillingDTO> getBill(
            @Parameter(description = "Bill ID") @PathVariable Long billId) {
        try {
            BillingDTO billing = billingService.getBillById(billId);
            return ResponseEntity.ok(billing);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get bills by branch", description = "Retrieve all bills for a specific branch with pagination")
    @RequireBranchAccess
    public ResponseEntity<Page<BillingDTO>> getBillsByBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId,
            Pageable pageable) {
        try {
            Page<BillingDTO> bills = billingService.getBillsByBranch(branchId, pageable);
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get bills by customer", description = "Retrieve all bills for a specific customer")
    @RequireBranchAccess
    public ResponseEntity<List<BillingDTO>> getBillsByCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long customerId) {
        try {
            List<BillingDTO> bills = billingService.getBillsByCustomer(customerId);
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/unpaid/branch/{branchId}")
    @Operation(summary = "Get unpaid bills", description = "Retrieve all unpaid and partially paid bills for a branch")
    @RequireBranchAccess
    public ResponseEntity<List<BillingDTO>> getUnpaidBills(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        try {
            List<BillingDTO> bills = billingService.getUnpaidBills(branchId);
            return ResponseEntity.ok(bills);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/report/branch/{branchId}")
    @Operation(summary = "Get billing report", description = "Generate comprehensive billing report for a branch")
    @RequireBranchAccess
    public ResponseEntity<BillingReportDTO> getBillingReport(
            @Parameter(description = "Branch ID") @PathVariable Long branchId,
            @Parameter(description = "Start date (yyyy-MM-dd)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            BillingReportDTO report = billingService.getBillingReport(branchId, startDate, endDate);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{billId}/refund")
    @Operation(summary = "Refund a bill", description = "Process refund for a paid bill")
    @RequireBranchAccess
    public ResponseEntity<BillingDTO> refundBill(
            @Parameter(description = "Bill ID") @PathVariable Long billId,
            @Parameter(description = "Refund reason") @RequestParam String reason) {
        try {
            BillingDTO billing = billingService.refundBill(billId, reason);
            return ResponseEntity.ok(billing);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/bill-number/{billNumber}")
    @Operation(summary = "Get bill by bill number", description = "Retrieve bill using bill number")
    @RequireBranchAccess
    public ResponseEntity<BillingDTO> getBillByNumber(
            @Parameter(description = "Bill number") @PathVariable String billNumber) {
        try {
            // This would need to be implemented in the service
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search bills", description = "Search bills with various filters")
    @RequireBranchAccess
    public ResponseEntity<Page<BillingDTO>> searchBills(
            @Parameter(description = "Branch ID") @RequestParam(required = false) Long branchId,
            @Parameter(description = "Customer ID") @RequestParam(required = false) Long customerId,
            @Parameter(description = "Status") @RequestParam(required = false) String status,
            @Parameter(description = "Payment method") @RequestParam(required = false) String paymentMethod,
            @Parameter(description = "Start date (yyyy-MM-dd)") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable) {
        try {
            // This would need to be implemented in the service
            return ResponseEntity.ok(Page.empty());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/dashboard/branch/{branchId}")
    @Operation(summary = "Get billing dashboard data", description = "Get summary data for billing dashboard")
    @RequireBranchAccess
    public ResponseEntity<BillingDashboardDTO> getBillingDashboard(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        try {
            // This would need to be implemented
            return ResponseEntity.ok(new BillingDashboardDTO());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{billId}/invoice/pdf")
    @Operation(summary = "Generate PDF invoice", description = "Generate and download PDF invoice for a bill")
    @RequireBranchAccess
    public ResponseEntity<byte[]> generateInvoicePdf(
            @Parameter(description = "Bill ID") @PathVariable Long billId) {
        try {
            byte[] pdfBytes = invoiceService.generateInvoicePdf(billId);
            return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=invoice-" + billId + ".pdf")
                .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{billId}/invoice/html")
    @Operation(summary = "Generate HTML invoice", description = "Generate HTML invoice for web display")
    @RequireBranchAccess
    public ResponseEntity<String> generateInvoiceHtml(
            @Parameter(description = "Bill ID") @PathVariable Long billId) {
        try {
            String htmlContent = invoiceService.generateInvoiceHtml(billId);
            return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(htmlContent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
