package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.LowStockAlertDTO;
import com.hexalyte.salon.model.LowStockAlert;
import com.hexalyte.salon.model.Inventory;
import com.hexalyte.salon.model.Product;
import com.hexalyte.salon.repository.LowStockAlertRepository;
import com.hexalyte.salon.repository.InventoryRepository;
import com.hexalyte.salon.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LowStockAlertService {
    
    @Autowired
    private LowStockAlertRepository lowStockAlertRepository;
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Check for low stock items and create alerts
     * This method is called automatically every hour
     */
    @Scheduled(fixedRate = 3600000) // Run every hour
    public void checkLowStockAlerts() {
        try {
            // Get all active products with alert quantities
            List<Product> productsWithAlerts = productRepository.findActiveProductsWithAlertQuantity();
            
            for (Product product : productsWithAlerts) {
                // Get all inventory entries for this product
                List<Inventory> inventoryEntries = inventoryRepository.findByProductId(product.getProductId());
                
                for (Inventory inventory : inventoryEntries) {
                    checkAndCreateAlert(inventory, product);
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking low stock alerts: " + e.getMessage());
        }
    }
    
    /**
     * Check a specific inventory entry and create alert if needed
     */
    public void checkAndCreateAlert(Inventory inventory, Product product) {
        try {
            // Check if there's already an unresolved alert for this product and branch
            boolean hasUnresolvedAlert = lowStockAlertRepository
                    .findUnresolvedAlertByBranchAndProduct(inventory.getBranchId(), product.getProductId())
                    .isPresent();
            
            if (hasUnresolvedAlert) {
                return; // Already has an alert
            }
            
            // Check if stock is below alert threshold
            if (inventory.getQuantity().compareTo(product.getAlertQuantity()) <= 0) {
                LowStockAlert.AlertType alertType = inventory.getQuantity().compareTo(BigDecimal.ZERO) == 0 ? 
                        LowStockAlert.AlertType.OUT_OF_STOCK : LowStockAlert.AlertType.LOW_STOCK;
                
                LowStockAlert alert = new LowStockAlert(
                        inventory.getBranchId(),
                        product.getProductId(),
                        inventory.getQuantity(),
                        product.getAlertQuantity(),
                        alertType
                );
                
                lowStockAlertRepository.save(alert);
                
                // Send notification
                sendLowStockNotification(alert, inventory, product);
            }
        } catch (Exception e) {
            System.err.println("Error creating low stock alert: " + e.getMessage());
        }
    }
    
    /**
     * Resolve a low stock alert
     */
    public void resolveAlert(Long alertId, Long resolvedBy) {
        LowStockAlert alert = lowStockAlertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Low stock alert not found with id: " + alertId));
        
        alert.setIsResolved(true);
        alert.setResolvedBy(resolvedBy);
        alert.setResolvedAt(LocalDateTime.now());
        
        lowStockAlertRepository.save(alert);
    }
    
    /**
     * Get all unresolved alerts
     */
    @Transactional(readOnly = true)
    public List<LowStockAlertDTO> getUnresolvedAlerts(Long branchId) {
        List<LowStockAlert> alerts = branchId != null ? 
                lowStockAlertRepository.findUnresolvedAlertsByBranch(branchId) : 
                lowStockAlertRepository.findUnresolvedAlerts();
        
        return alerts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get alert statistics
     */
    @Transactional(readOnly = true)
    public LowStockAlertStatsDTO getAlertStats(Long branchId) {
        LowStockAlertStatsDTO stats = new LowStockAlertStatsDTO();
        
        stats.setTotalUnresolvedAlerts(
                branchId != null ? 
                        lowStockAlertRepository.countUnresolvedAlertsByBranch(branchId) : 
                        lowStockAlertRepository.countUnresolvedAlerts()
        );
        
        stats.setLowStockAlerts(
                branchId != null ? 
                        lowStockAlertRepository.countLowStockAlertsByBranch(branchId) : 
                        lowStockAlertRepository.countUnresolvedAlerts() // This would need a separate method for all branches
        );
        
        stats.setOutOfStockAlerts(
                branchId != null ? 
                        lowStockAlertRepository.countOutOfStockAlertsByBranch(branchId) : 
                        0L // This would need a separate method for all branches
        );
        
        return stats;
    }
    
    /**
     * Send low stock notification
     */
    private void sendLowStockNotification(LowStockAlert alert, Inventory inventory, Product product) {
        try {
            String message = String.format(
                    "Low Stock Alert: %s (%s) in %s branch has %s %s remaining (Alert level: %s %s)",
                    product.getName(),
                    product.getCode(),
                    inventory.getBranch().getBranchName(),
                    inventory.getQuantity(),
                    product.getUom(),
                    product.getAlertQuantity(),
                    product.getUom()
            );
            
            // Send notification to branch managers and admins
            notificationService.sendLowStockAlert(alert, message);
        } catch (Exception e) {
            System.err.println("Error sending low stock notification: " + e.getMessage());
        }
    }
    
    /**
     * Convert LowStockAlert to DTO
     */
    private LowStockAlertDTO convertToDTO(LowStockAlert alert) {
        LowStockAlertDTO dto = new LowStockAlertDTO();
        dto.setId(alert.getId());
        dto.setBranchId(alert.getBranchId());
        dto.setProductId(alert.getProductId());
        dto.setCurrentQuantity(alert.getCurrentQuantity());
        dto.setAlertQuantity(alert.getAlertQuantity());
        dto.setAlertType(alert.getAlertType().name());
        dto.setIsResolved(alert.getIsResolved());
        dto.setResolvedAt(alert.getResolvedAt());
        dto.setResolvedBy(alert.getResolvedBy());
        dto.setCreatedAt(alert.getCreatedAt());
        
        // Set additional fields if loaded
        if (alert.getProduct() != null) {
            dto.setProductName(alert.getProduct().getName());
            dto.setProductCode(alert.getProduct().getCode());
            dto.setProductUom(alert.getProduct().getUom());
        }
        
        if (alert.getBranch() != null) {
            dto.setBranchName(alert.getBranch().getBranchName());
            dto.setBranchCode(alert.getBranch().getBranchCode());
        }
        
        return dto;
    }
    
    /**
     * DTO for alert statistics
     */
    public static class LowStockAlertStatsDTO {
        private Long totalUnresolvedAlerts;
        private Long lowStockAlerts;
        private Long outOfStockAlerts;
        
        // Getters and setters
        public Long getTotalUnresolvedAlerts() {
            return totalUnresolvedAlerts;
        }
        
        public void setTotalUnresolvedAlerts(Long totalUnresolvedAlerts) {
            this.totalUnresolvedAlerts = totalUnresolvedAlerts;
        }
        
        public Long getLowStockAlerts() {
            return lowStockAlerts;
        }
        
        public void setLowStockAlerts(Long lowStockAlerts) {
            this.lowStockAlerts = lowStockAlerts;
        }
        
        public Long getOutOfStockAlerts() {
            return outOfStockAlerts;
        }
        
        public void setOutOfStockAlerts(Long outOfStockAlerts) {
            this.outOfStockAlerts = outOfStockAlerts;
        }
    }
}
