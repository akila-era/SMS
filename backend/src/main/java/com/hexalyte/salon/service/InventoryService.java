package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.InventoryDTO;
import com.hexalyte.salon.dto.InventoryDashboardDTO;
import com.hexalyte.salon.dto.LowStockAlertDTO;
import com.hexalyte.salon.model.Inventory;
import com.hexalyte.salon.model.LowStockAlert;
import com.hexalyte.salon.model.Product;
import com.hexalyte.salon.repository.InventoryRepository;
import com.hexalyte.salon.repository.LowStockAlertRepository;
import com.hexalyte.salon.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryService {
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private LowStockAlertRepository lowStockAlertRepository;
    
    @Autowired
    private InventoryTransactionService inventoryTransactionService;
    
    @Transactional(readOnly = true)
    public InventoryDTO getInventoryByBranchAndProduct(Long branchId, Long productId) {
        Inventory inventory = inventoryRepository.findByBranchIdAndProductId(branchId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for branch: " + branchId + " and product: " + productId));
        return convertToDTO(inventory);
    }
    
    @Transactional(readOnly = true)
    public List<InventoryDTO> getInventoryByBranch(Long branchId) {
        return inventoryRepository.findByBranchId(branchId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<InventoryDTO> getInventoryByBranchWithFilters(Long branchId, String productName, 
                                                            String category, String productType, Pageable pageable) {
        return inventoryRepository.findByBranchIdAndFilters(branchId, productName, category, productType, pageable)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<InventoryDTO> getInventoryWithFilters(Long branchId, String productName, 
                                                    String category, String productType, Pageable pageable) {
        return inventoryRepository.findByFilters(branchId, productName, category, productType, pageable)
                .map(this::convertToDTO);
    }
    
    public InventoryDTO updateInventoryQuantity(Long branchId, Long productId, BigDecimal newQuantity, 
                                             String remarks, Long userId) {
        Inventory inventory = inventoryRepository.findByBranchIdAndProductId(branchId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for branch: " + branchId + " and product: " + productId));
        
        BigDecimal oldQuantity = inventory.getQuantity();
        BigDecimal quantityDifference = newQuantity.subtract(oldQuantity);
        
        // Update inventory quantity
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);
        
        // Create transaction record
        inventoryTransactionService.createAdjustmentTransaction(
                branchId, productId, quantityDifference, remarks, userId);
        
        // Check for low stock alerts
        checkAndCreateLowStockAlert(branchId, productId, newQuantity);
        
        return convertToDTO(inventory);
    }
    
    public InventoryDTO addInventoryQuantity(Long branchId, Long productId, BigDecimal quantityToAdd, 
                                          String remarks, Long userId) {
        Inventory inventory = inventoryRepository.findByBranchIdAndProductId(branchId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for branch: " + branchId + " and product: " + productId));
        
        BigDecimal newQuantity = inventory.getQuantity().add(quantityToAdd);
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);
        
        // Create transaction record
        inventoryTransactionService.createAdjustmentTransaction(
                branchId, productId, quantityToAdd, remarks, userId);
        
        // Check for low stock alerts
        checkAndCreateLowStockAlert(branchId, productId, newQuantity);
        
        return convertToDTO(inventory);
    }
    
    public InventoryDTO deductInventoryQuantity(Long branchId, Long productId, BigDecimal quantityToDeduct, 
                                             String remarks, Long userId) {
        Inventory inventory = inventoryRepository.findByBranchIdAndProductId(branchId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for branch: " + branchId + " and product: " + productId));
        
        if (inventory.getQuantity().compareTo(quantityToDeduct) < 0) {
            throw new IllegalArgumentException("Insufficient inventory quantity. Available: " + inventory.getQuantity() + 
                                             ", Required: " + quantityToDeduct);
        }
        
        BigDecimal newQuantity = inventory.getQuantity().subtract(quantityToDeduct);
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);
        
        // Create transaction record
        inventoryTransactionService.createAdjustmentTransaction(
                branchId, productId, quantityToDeduct.negate(), remarks, userId);
        
        // Check for low stock alerts
        checkAndCreateLowStockAlert(branchId, productId, newQuantity);
        
        return convertToDTO(inventory);
    }
    
    @Transactional(readOnly = true)
    public List<InventoryDTO> getLowStockItems(Long branchId) {
        List<Inventory> lowStockItems = branchId != null ? 
                inventoryRepository.findLowStockItemsByBranch(branchId) : 
                inventoryRepository.findLowStockItems();
        
        return lowStockItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InventoryDTO> getOutOfStockItems(Long branchId) {
        List<Inventory> outOfStockItems = branchId != null ? 
                inventoryRepository.findOutOfStockItemsByBranch(branchId) : 
                inventoryRepository.findOutOfStockItems();
        
        return outOfStockItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalInventoryValue(Long branchId) {
        return branchId != null ? 
                inventoryRepository.calculateTotalValueByBranch(branchId) : 
                inventoryRepository.calculateTotalValue();
    }
    
    @Transactional(readOnly = true)
    public InventoryDashboardDTO getInventoryDashboard(Long branchId) {
        InventoryDashboardDTO dashboard = new InventoryDashboardDTO();
        
        // Get basic counts
        Long totalProducts = branchId != null ? 
                inventoryRepository.countByBranchId(branchId) : 
                inventoryRepository.count();
        
        Long lowStockCount = branchId != null ? 
                inventoryRepository.countLowStockByBranchId(branchId) : 
                lowStockAlertRepository.countUnresolvedAlerts();
        
        Long outOfStockCount = branchId != null ? 
                inventoryRepository.countOutOfStockByBranchId(branchId) : 
                inventoryRepository.findOutOfStockItems().size();
        
        BigDecimal totalValue = getTotalInventoryValue(branchId);
        
        dashboard.setTotalProducts(totalProducts);
        dashboard.setLowStockCount(lowStockCount);
        dashboard.setOutOfStockCount(outOfStockCount);
        dashboard.setTotalStockValue(totalValue);
        
        // Get low stock alerts
        List<LowStockAlertDTO> alerts = getLowStockAlerts(branchId);
        dashboard.setLowStockAlerts(alerts);
        
        return dashboard;
    }
    
    @Transactional(readOnly = true)
    public List<LowStockAlertDTO> getLowStockAlerts(Long branchId) {
        List<LowStockAlert> alerts = branchId != null ? 
                lowStockAlertRepository.findUnresolvedAlertsByBranch(branchId) : 
                lowStockAlertRepository.findUnresolvedAlerts();
        
        return alerts.stream()
                .map(this::convertAlertToDTO)
                .collect(Collectors.toList());
    }
    
    public void resolveLowStockAlert(Long alertId, Long resolvedBy) {
        LowStockAlert alert = lowStockAlertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Low stock alert not found with id: " + alertId));
        
        alert.setIsResolved(true);
        alert.setResolvedBy(resolvedBy);
        alert.setResolvedAt(java.time.LocalDateTime.now());
        
        lowStockAlertRepository.save(alert);
    }
    
    private void checkAndCreateLowStockAlert(Long branchId, Long productId, BigDecimal currentQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        
        if (product.getAlertQuantity().compareTo(BigDecimal.ZERO) > 0) {
            // Check if there's already an unresolved alert for this product
            Optional<LowStockAlert> existingAlert = lowStockAlertRepository
                    .findUnresolvedAlertByBranchAndProduct(branchId, productId);
            
            if (currentQuantity.compareTo(product.getAlertQuantity()) <= 0) {
                // Create or update low stock alert
                LowStockAlert.AlertType alertType = currentQuantity.compareTo(BigDecimal.ZERO) == 0 ? 
                        LowStockAlert.AlertType.OUT_OF_STOCK : LowStockAlert.AlertType.LOW_STOCK;
                
                if (existingAlert.isPresent()) {
                    LowStockAlert alert = existingAlert.get();
                    alert.setCurrentQuantity(currentQuantity);
                    alert.setAlertType(alertType);
                    lowStockAlertRepository.save(alert);
                } else {
                    LowStockAlert alert = new LowStockAlert(branchId, productId, currentQuantity, 
                                                          product.getAlertQuantity(), alertType);
                    lowStockAlertRepository.save(alert);
                }
            } else if (existingAlert.isPresent()) {
                // Resolve existing alert if stock is now above threshold
                LowStockAlert alert = existingAlert.get();
                alert.setIsResolved(true);
                alert.setResolvedAt(java.time.LocalDateTime.now());
                lowStockAlertRepository.save(alert);
            }
        }
    }
    
    // Helper methods
    private InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setBranchId(inventory.getBranchId());
        dto.setProductId(inventory.getProductId());
        dto.setQuantity(inventory.getQuantity());
        dto.setLastUpdated(inventory.getLastUpdated());
        
        // Set additional fields if product is loaded
        if (inventory.getProduct() != null) {
            dto.setProductName(inventory.getProduct().getName());
            dto.setProductCode(inventory.getProduct().getCode());
            dto.setProductCategory(inventory.getProduct().getCategory());
            dto.setProductBrand(inventory.getProduct().getBrand());
            dto.setProductUom(inventory.getProduct().getUom());
            dto.setProductType(inventory.getProduct().getProductType().name());
            dto.setProductCostPrice(inventory.getProduct().getCostPrice());
            dto.setProductAlertQuantity(inventory.getProduct().getAlertQuantity());
        }
        
        // Set additional fields if branch is loaded
        if (inventory.getBranch() != null) {
            dto.setBranchName(inventory.getBranch().getBranchName());
            dto.setBranchCode(inventory.getBranch().getBranchCode());
        }
        
        return dto;
    }
    
    private LowStockAlertDTO convertAlertToDTO(LowStockAlert alert) {
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
        
        // Set additional fields if product is loaded
        if (alert.getProduct() != null) {
            dto.setProductName(alert.getProduct().getName());
            dto.setProductCode(alert.getProduct().getCode());
            dto.setProductUom(alert.getProduct().getUom());
        }
        
        // Set additional fields if branch is loaded
        if (alert.getBranch() != null) {
            dto.setBranchName(alert.getBranch().getBranchName());
            dto.setBranchCode(alert.getBranch().getBranchCode());
        }
        
        return dto;
    }
}
