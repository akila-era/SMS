package com.hexalyte.salon.service;

import com.hexalyte.salon.model.Billing;
import com.hexalyte.salon.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class InvoiceService {
    
    @Autowired
    private BillingRepository billingRepository;
    
    /**
     * Generate PDF invoice for a bill
     */
    public byte[] generateInvoicePdf(Long billId) {
        Billing billing = billingRepository.findById(billId)
            .orElseThrow(() -> new RuntimeException("Bill not found"));
        
        // For now, return a simple text-based invoice
        // In a real implementation, you would use a PDF library like iText or Apache PDFBox
        String invoiceContent = generateInvoiceContent(billing);
        return invoiceContent.getBytes();
    }
    
    /**
     * Generate HTML invoice for web display
     */
    public String generateInvoiceHtml(Long billId) {
        Billing billing = billingRepository.findById(billId)
            .orElseThrow(() -> new RuntimeException("Bill not found"));
        
        return generateInvoiceHtml(billing);
    }
    
    /**
     * Generate invoice content as text
     */
    private String generateInvoiceContent(Billing billing) {
        StringBuilder content = new StringBuilder();
        
        // Header
        content.append("=".repeat(50)).append("\n");
        content.append("           SALON MANAGEMENT SYSTEM\n");
        content.append("=".repeat(50)).append("\n");
        content.append("Bill Number: ").append(billing.getBillNumber()).append("\n");
        content.append("Date: ").append(billing.getBillDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        content.append("Branch: ").append(billing.getBranch().getBranchName()).append("\n");
        content.append("-".repeat(50)).append("\n");
        
        // Customer details
        content.append("Customer: ").append(billing.getCustomer().getFullName()).append("\n");
        content.append("Phone: ").append(billing.getCustomer().getPhone()).append("\n");
        if (billing.getCustomer().getEmail() != null) {
            content.append("Email: ").append(billing.getCustomer().getEmail()).append("\n");
        }
        content.append("-".repeat(50)).append("\n");
        
        // Services
        content.append("SERVICES:\n");
        content.append(String.format("%-20s %-15s %-8s %-10s\n", "Service", "Staff", "Qty", "Amount"));
        content.append("-".repeat(50)).append("\n");
        
        for (var item : billing.getBillingItems()) {
            content.append(String.format("%-20s %-15s %-8d %-10.2f\n",
                item.getService().getName(),
                item.getStaff().getFullName(),
                item.getQuantity(),
                item.getTotal().doubleValue()
            ));
        }
        
        content.append("-".repeat(50)).append("\n");
        
        // Totals
        content.append(String.format("%-35s %-10.2f\n", "Subtotal:", billing.getSubtotal().doubleValue()));
        
        if (billing.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
            content.append(String.format("%-35s %-10.2f\n", "Discount:", -billing.getDiscountAmount().doubleValue()));
        }
        
        if (billing.getLoyaltyRedeemed().compareTo(BigDecimal.ZERO) > 0) {
            content.append(String.format("%-35s %-10.2f\n", "Loyalty Redeemed:", -billing.getLoyaltyRedeemed().doubleValue()));
        }
        
        content.append(String.format("%-35s %-10.2f\n", "Tax:", billing.getTaxAmount().doubleValue()));
        content.append("=".repeat(50)).append("\n");
        content.append(String.format("%-35s %-10.2f\n", "TOTAL:", billing.getTotalAmount().doubleValue()));
        content.append("=".repeat(50)).append("\n");
        
        content.append(String.format("%-35s %-10.2f\n", "Paid:", billing.getPaidAmount().doubleValue()));
        
        if (billing.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0) {
            content.append(String.format("%-35s %-10.2f\n", "Balance:", billing.getBalanceAmount().doubleValue()));
        }
        
        content.append("\n");
        content.append("Status: ").append(billing.getStatus()).append("\n");
        if (billing.getPaymentMethod() != null) {
            content.append("Payment Method: ").append(billing.getPaymentMethod()).append("\n");
        }
        
        content.append("\n");
        content.append("Thank you for your business!\n");
        content.append("=".repeat(50)).append("\n");
        
        return content.toString();
    }
    
    /**
     * Generate HTML invoice
     */
    private String generateInvoiceHtml(Billing billing) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<title>Invoice - ").append(billing.getBillNumber()).append("</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append(".header { text-align: center; margin-bottom: 30px; }");
        html.append(".invoice-details { margin-bottom: 20px; }");
        html.append(".customer-details { margin-bottom: 20px; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }");
        html.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append(".totals { margin-top: 20px; }");
        html.append(".total-row { font-weight: bold; font-size: 1.1em; }");
        html.append(".footer { margin-top: 30px; text-align: center; }");
        html.append("</style>");
        html.append("</head><body>");
        
        // Header
        html.append("<div class='header'>");
        html.append("<h1>SALON MANAGEMENT SYSTEM</h1>");
        html.append("<h2>INVOICE</h2>");
        html.append("</div>");
        
        // Invoice details
        html.append("<div class='invoice-details'>");
        html.append("<p><strong>Bill Number:</strong> ").append(billing.getBillNumber()).append("</p>");
        html.append("<p><strong>Date:</strong> ").append(billing.getBillDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("</p>");
        html.append("<p><strong>Branch:</strong> ").append(billing.getBranch().getBranchName()).append("</p>");
        html.append("</div>");
        
        // Customer details
        html.append("<div class='customer-details'>");
        html.append("<h3>Customer Details</h3>");
        html.append("<p><strong>Name:</strong> ").append(billing.getCustomer().getFullName()).append("</p>");
        html.append("<p><strong>Phone:</strong> ").append(billing.getCustomer().getPhone()).append("</p>");
        if (billing.getCustomer().getEmail() != null) {
            html.append("<p><strong>Email:</strong> ").append(billing.getCustomer().getEmail()).append("</p>");
        }
        html.append("</div>");
        
        // Services table
        html.append("<table>");
        html.append("<thead><tr>");
        html.append("<th>Service</th>");
        html.append("<th>Staff</th>");
        html.append("<th>Quantity</th>");
        html.append("<th>Unit Price</th>");
        html.append("<th>Total</th>");
        html.append("</tr></thead><tbody>");
        
        for (var item : billing.getBillingItems()) {
            html.append("<tr>");
            html.append("<td>").append(item.getService().getName()).append("</td>");
            html.append("<td>").append(item.getStaff().getFullName()).append("</td>");
            html.append("<td>").append(item.getQuantity()).append("</td>");
            html.append("<td>").append(String.format("%.2f", item.getUnitPrice().doubleValue())).append("</td>");
            html.append("<td>").append(String.format("%.2f", item.getTotal().doubleValue())).append("</td>");
            html.append("</tr>");
        }
        
        html.append("</tbody></table>");
        
        // Totals
        html.append("<div class='totals'>");
        html.append("<table>");
        html.append("<tr><td>Subtotal:</td><td>").append(String.format("%.2f", billing.getSubtotal().doubleValue())).append("</td></tr>");
        
        if (billing.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
            html.append("<tr><td>Discount:</td><td>-").append(String.format("%.2f", billing.getDiscountAmount().doubleValue())).append("</td></tr>");
        }
        
        if (billing.getLoyaltyRedeemed().compareTo(BigDecimal.ZERO) > 0) {
            html.append("<tr><td>Loyalty Redeemed:</td><td>-").append(String.format("%.2f", billing.getLoyaltyRedeemed().doubleValue())).append("</td></tr>");
        }
        
        html.append("<tr><td>Tax:</td><td>").append(String.format("%.2f", billing.getTaxAmount().doubleValue())).append("</td></tr>");
        html.append("<tr class='total-row'><td><strong>TOTAL:</strong></td><td><strong>").append(String.format("%.2f", billing.getTotalAmount().doubleValue())).append("</strong></td></tr>");
        html.append("<tr><td>Paid:</td><td>").append(String.format("%.2f", billing.getPaidAmount().doubleValue())).append("</td></tr>");
        
        if (billing.getBalanceAmount().compareTo(BigDecimal.ZERO) > 0) {
            html.append("<tr><td>Balance:</td><td>").append(String.format("%.2f", billing.getBalanceAmount().doubleValue())).append("</td></tr>");
        }
        
        html.append("</table>");
        html.append("</div>");
        
        // Footer
        html.append("<div class='footer'>");
        html.append("<p><strong>Status:</strong> ").append(billing.getStatus()).append("</p>");
        if (billing.getPaymentMethod() != null) {
            html.append("<p><strong>Payment Method:</strong> ").append(billing.getPaymentMethod()).append("</p>");
        }
        html.append("<p>Thank you for your business!</p>");
        html.append("</div>");
        
        html.append("</body></html>");
        
        return html.toString();
    }
}
