package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.InventoryUsageDTO;
import com.hexalyte.salon.dto.InventoryDashboardDTO;
import com.hexalyte.salon.service.InventoryUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/inventory/usage")
@CrossOrigin(origins = "*")
public class InventoryUsageController {
    
    @Autowired
    private InventoryUsageService inventoryUsageService;
    
    @PostMapping
    public ResponseEntity<InventoryUsageDTO> recordProductUsage(
            @RequestParam Long appointmentId,
            @RequestParam Long serviceId,
            @RequestParam Long staffId,
            @RequestParam Long branchId,
            @RequestParam Long productId,
            @RequestParam BigDecimal quantityUsed,
            @RequestParam BigDecimal unitCost) {
        try {
            InventoryUsageDTO usage = inventoryUsageService.recordProductUsage(
                    appointmentId, serviceId, staffId, branchId, productId, quantityUsed, unitCost);
            return new ResponseEntity<>(usage, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<InventoryUsageDTO>> getUsageByAppointment(@PathVariable Long appointmentId) {
        List<InventoryUsageDTO> usage = inventoryUsageService.getUsageByAppointment(appointmentId);
        return new ResponseEntity<>(usage, HttpStatus.OK);
    }
    
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<InventoryUsageDTO>> getUsageByService(@PathVariable Long serviceId) {
        List<InventoryUsageDTO> usage = inventoryUsageService.getUsageByService(serviceId);
        return new ResponseEntity<>(usage, HttpStatus.OK);
    }
    
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<InventoryUsageDTO>> getUsageByStaff(@PathVariable Long staffId) {
        List<InventoryUsageDTO> usage = inventoryUsageService.getUsageByStaff(staffId);
        return new ResponseEntity<>(usage, HttpStatus.OK);
    }
    
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<InventoryUsageDTO>> getUsageByBranch(@PathVariable Long branchId) {
        List<InventoryUsageDTO> usage = inventoryUsageService.getUsageByBranch(branchId);
        return new ResponseEntity<>(usage, HttpStatus.OK);
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<InventoryUsageDTO>> getUsageByProduct(@PathVariable Long productId) {
        List<InventoryUsageDTO> usage = inventoryUsageService.getUsageByProduct(productId);
        return new ResponseEntity<>(usage, HttpStatus.OK);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<InventoryUsageDTO>> getUsageByFilters(
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) Long staffId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            Pageable pageable) {
        Page<InventoryUsageDTO> usage = inventoryUsageService.getUsageByFilters(
                branchId, productId, serviceId, staffId, fromDate, toDate, pageable);
        return new ResponseEntity<>(usage, HttpStatus.OK);
    }
    
    @GetMapping("/branch/{branchId}/date-range")
    public ResponseEntity<List<InventoryUsageDTO>> getUsageByBranchAndDateRange(
            @PathVariable Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        List<InventoryUsageDTO> usage = inventoryUsageService
                .getUsageByBranchAndDateRange(branchId, fromDate, toDate);
        return new ResponseEntity<>(usage, HttpStatus.OK);
    }
    
    @GetMapping("/branch/{branchId}/recent")
    public ResponseEntity<List<InventoryUsageDTO>> getRecentUsageByBranch(
            @PathVariable Long branchId,
            Pageable pageable) {
        List<InventoryUsageDTO> usage = inventoryUsageService
                .getRecentUsageByBranch(branchId, pageable);
        return new ResponseEntity<>(usage, HttpStatus.OK);
    }
    
    @GetMapping("/branch/{branchId}/total-cost")
    public ResponseEntity<BigDecimal> getTotalUsageCost(
            @PathVariable Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        BigDecimal totalCost = inventoryUsageService.getTotalUsageCost(branchId, fromDate, toDate);
        return new ResponseEntity<>(totalCost, HttpStatus.OK);
    }
    
    @GetMapping("/top-used-products")
    public ResponseEntity<List<InventoryDashboardDTO.ProductUsageSummaryDTO>> getTopUsedProducts(
            @RequestParam(required = false) Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            Pageable pageable) {
        List<InventoryDashboardDTO.ProductUsageSummaryDTO> topProducts = inventoryUsageService
                .getTopUsedProducts(branchId, fromDate, toDate, pageable);
        return new ResponseEntity<>(topProducts, HttpStatus.OK);
    }
}
