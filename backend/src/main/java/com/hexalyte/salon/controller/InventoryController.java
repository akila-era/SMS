package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.InventoryDTO;
import com.hexalyte.salon.dto.InventoryDashboardDTO;
import com.hexalyte.salon.dto.LowStockAlertDTO;
import com.hexalyte.salon.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    @GetMapping("/branch/{branchId}/product/{productId}")
    public ResponseEntity<InventoryDTO> getInventoryByBranchAndProduct(@PathVariable Long branchId, 
                                                                     @PathVariable Long productId) {
        try {
            InventoryDTO inventory = inventoryService.getInventoryByBranchAndProduct(branchId, productId);
            return new ResponseEntity<>(inventory, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<InventoryDTO>> getInventoryByBranch(@PathVariable Long branchId) {
        List<InventoryDTO> inventory = inventoryService.getInventoryByBranch(branchId);
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }
    
    @GetMapping("/branch/{branchId}/search")
    public ResponseEntity<Page<InventoryDTO>> getInventoryByBranchWithFilters(
            @PathVariable Long branchId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String productType,
            Pageable pageable) {
        Page<InventoryDTO> inventory = inventoryService.getInventoryByBranchWithFilters(
                branchId, productName, category, productType, pageable);
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<InventoryDTO>> getInventoryWithFilters(
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String productType,
            Pageable pageable) {
        Page<InventoryDTO> inventory = inventoryService.getInventoryWithFilters(
                branchId, productName, category, productType, pageable);
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }
    
    @PutMapping("/branch/{branchId}/product/{productId}/quantity")
    public ResponseEntity<InventoryDTO> updateInventoryQuantity(
            @PathVariable Long branchId,
            @PathVariable Long productId,
            @RequestParam BigDecimal newQuantity,
            @RequestParam(required = false) String remarks,
            @RequestParam Long userId) {
        try {
            InventoryDTO inventory = inventoryService.updateInventoryQuantity(
                    branchId, productId, newQuantity, remarks, userId);
            return new ResponseEntity<>(inventory, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/branch/{branchId}/product/{productId}/add")
    public ResponseEntity<InventoryDTO> addInventoryQuantity(
            @PathVariable Long branchId,
            @PathVariable Long productId,
            @RequestParam BigDecimal quantityToAdd,
            @RequestParam(required = false) String remarks,
            @RequestParam Long userId) {
        try {
            InventoryDTO inventory = inventoryService.addInventoryQuantity(
                    branchId, productId, quantityToAdd, remarks, userId);
            return new ResponseEntity<>(inventory, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/branch/{branchId}/product/{productId}/deduct")
    public ResponseEntity<InventoryDTO> deductInventoryQuantity(
            @PathVariable Long branchId,
            @PathVariable Long productId,
            @RequestParam BigDecimal quantityToDeduct,
            @RequestParam(required = false) String remarks,
            @RequestParam Long userId) {
        try {
            InventoryDTO inventory = inventoryService.deductInventoryQuantity(
                    branchId, productId, quantityToDeduct, remarks, userId);
            return new ResponseEntity<>(inventory, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryDTO>> getLowStockItems(@RequestParam(required = false) Long branchId) {
        List<InventoryDTO> lowStockItems = inventoryService.getLowStockItems(branchId);
        return new ResponseEntity<>(lowStockItems, HttpStatus.OK);
    }
    
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<InventoryDTO>> getOutOfStockItems(@RequestParam(required = false) Long branchId) {
        List<InventoryDTO> outOfStockItems = inventoryService.getOutOfStockItems(branchId);
        return new ResponseEntity<>(outOfStockItems, HttpStatus.OK);
    }
    
    @GetMapping("/total-value")
    public ResponseEntity<BigDecimal> getTotalInventoryValue(@RequestParam(required = false) Long branchId) {
        BigDecimal totalValue = inventoryService.getTotalInventoryValue(branchId);
        return new ResponseEntity<>(totalValue, HttpStatus.OK);
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<InventoryDashboardDTO> getInventoryDashboard(@RequestParam(required = false) Long branchId) {
        InventoryDashboardDTO dashboard = inventoryService.getInventoryDashboard(branchId);
        return new ResponseEntity<>(dashboard, HttpStatus.OK);
    }
    
    @GetMapping("/alerts")
    public ResponseEntity<List<LowStockAlertDTO>> getLowStockAlerts(@RequestParam(required = false) Long branchId) {
        List<LowStockAlertDTO> alerts = inventoryService.getLowStockAlerts(branchId);
        return new ResponseEntity<>(alerts, HttpStatus.OK);
    }
    
    @PutMapping("/alerts/{alertId}/resolve")
    public ResponseEntity<Void> resolveLowStockAlert(@PathVariable Long alertId, 
                                                   @RequestParam Long resolvedBy) {
        try {
            inventoryService.resolveLowStockAlert(alertId, resolvedBy);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
