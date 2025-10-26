package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.InventoryTransactionDTO;
import com.hexalyte.salon.dto.PurchaseEntryDTO;
import com.hexalyte.salon.service.InventoryTransactionService;
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
@RequestMapping("/api/inventory/transactions")
@CrossOrigin(origins = "*")
public class InventoryTransactionController {
    
    @Autowired
    private InventoryTransactionService inventoryTransactionService;
    
    @PostMapping("/purchase")
    public ResponseEntity<InventoryTransactionDTO> createPurchaseTransaction(
            @RequestParam Long branchId,
            @RequestParam Long productId,
            @RequestParam BigDecimal quantity,
            @RequestParam BigDecimal unitPrice,
            @RequestParam BigDecimal totalAmount,
            @RequestParam Long supplierId,
            @RequestParam(required = false) String referenceNumber,
            @RequestParam(required = false) String remarks,
            @RequestParam Long createdBy) {
        try {
            InventoryTransactionDTO transaction = inventoryTransactionService.createPurchaseTransaction(
                    branchId, productId, quantity, unitPrice, totalAmount, supplierId, 
                    referenceNumber, remarks, createdBy);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/adjustment")
    public ResponseEntity<InventoryTransactionDTO> createAdjustmentTransaction(
            @RequestParam Long branchId,
            @RequestParam Long productId,
            @RequestParam BigDecimal quantity,
            @RequestParam(required = false) String remarks,
            @RequestParam Long createdBy) {
        try {
            InventoryTransactionDTO transaction = inventoryTransactionService.createAdjustmentTransaction(
                    branchId, productId, quantity, remarks, createdBy);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<InventoryTransactionDTO> createTransferTransaction(
            @RequestParam Long fromBranchId,
            @RequestParam Long toBranchId,
            @RequestParam Long productId,
            @RequestParam BigDecimal quantity,
            @RequestParam(required = false) String remarks,
            @RequestParam Long createdBy) {
        try {
            InventoryTransactionDTO transaction = inventoryTransactionService.createTransferTransaction(
                    fromBranchId, toBranchId, productId, quantity, remarks, createdBy);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/usage")
    public ResponseEntity<InventoryTransactionDTO> createUsageTransaction(
            @RequestParam Long branchId,
            @RequestParam Long productId,
            @RequestParam BigDecimal quantity,
            @RequestParam(required = false) String remarks,
            @RequestParam Long createdBy) {
        try {
            InventoryTransactionDTO transaction = inventoryTransactionService.createUsageTransaction(
                    branchId, productId, quantity, remarks, createdBy);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/purchase-entry")
    public ResponseEntity<PurchaseEntryDTO> processPurchaseEntry(@RequestBody PurchaseEntryDTO purchaseEntry) {
        try {
            PurchaseEntryDTO processedEntry = inventoryTransactionService.processPurchaseEntry(purchaseEntry);
            return new ResponseEntity<>(processedEntry, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<InventoryTransactionDTO>> getTransactionsByFilters(
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            Pageable pageable) {
        Page<InventoryTransactionDTO> transactions = inventoryTransactionService.getTransactionsByFilters(
                branchId, productId, type, supplierId, fromDate, toDate, pageable);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    
    @GetMapping("/branch/{branchId}/date-range")
    public ResponseEntity<List<InventoryTransactionDTO>> getTransactionsByBranchAndDateRange(
            @PathVariable Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        List<InventoryTransactionDTO> transactions = inventoryTransactionService
                .getTransactionsByBranchAndDateRange(branchId, fromDate, toDate);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    
    @GetMapping("/branch/{branchId}/recent")
    public ResponseEntity<List<InventoryTransactionDTO>> getRecentTransactionsByBranch(
            @PathVariable Long branchId,
            Pageable pageable) {
        List<InventoryTransactionDTO> transactions = inventoryTransactionService
                .getRecentTransactionsByBranch(branchId, pageable);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    
    @GetMapping("/branch/{branchId}/purchase-total")
    public ResponseEntity<BigDecimal> getTotalPurchaseAmount(
            @PathVariable Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        BigDecimal totalAmount = inventoryTransactionService.getTotalPurchaseAmount(branchId, fromDate, toDate);
        return new ResponseEntity<>(totalAmount, HttpStatus.OK);
    }
    
    @GetMapping("/branch/{branchId}/purchase-count")
    public ResponseEntity<Long> getPurchaseCount(
            @PathVariable Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        Long count = inventoryTransactionService.getPurchaseCount(branchId, fromDate, toDate);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
