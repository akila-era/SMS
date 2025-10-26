package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.LowStockAlertDTO;
import com.hexalyte.salon.service.LowStockAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/alerts")
@CrossOrigin(origins = "*")
public class LowStockAlertController {
    
    @Autowired
    private LowStockAlertService lowStockAlertService;
    
    @GetMapping
    public ResponseEntity<List<LowStockAlertDTO>> getUnresolvedAlerts(@RequestParam(required = false) Long branchId) {
        List<LowStockAlertDTO> alerts = lowStockAlertService.getUnresolvedAlerts(branchId);
        return new ResponseEntity<>(alerts, HttpStatus.OK);
    }
    
    @GetMapping("/stats")
    public ResponseEntity<LowStockAlertService.LowStockAlertStatsDTO> getAlertStats(@RequestParam(required = false) Long branchId) {
        LowStockAlertService.LowStockAlertStatsDTO stats = lowStockAlertService.getAlertStats(branchId);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @PutMapping("/{alertId}/resolve")
    public ResponseEntity<Void> resolveAlert(@PathVariable Long alertId, @RequestParam Long resolvedBy) {
        try {
            lowStockAlertService.resolveAlert(alertId, resolvedBy);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/check")
    public ResponseEntity<Void> checkLowStockAlerts() {
        lowStockAlertService.checkLowStockAlerts();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
