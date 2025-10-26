package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.InventoryTransactionDTO;
import com.hexalyte.salon.dto.PurchaseEntryDTO;
import com.hexalyte.salon.model.Inventory;
import com.hexalyte.salon.model.InventoryTransaction;
import com.hexalyte.salon.repository.InventoryRepository;
import com.hexalyte.salon.repository.InventoryTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryTransactionService {
    
    @Autowired
    private InventoryTransactionRepository inventoryTransactionRepository;
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private InventoryService inventoryService;
    
    public InventoryTransactionDTO createPurchaseTransaction(Long branchId, Long productId, 
                                                          BigDecimal quantity, BigDecimal unitPrice, 
                                                          BigDecimal totalAmount, Long supplierId, 
                                                          String referenceNumber, String remarks, Long createdBy) {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setBranchId(branchId);
        transaction.setProductId(productId);
        transaction.setType(InventoryTransaction.TransactionType.PURCHASE);
        transaction.setQuantity(quantity);
        transaction.setUnitPrice(unitPrice);
        transaction.setTotalAmount(totalAmount);
        transaction.setSupplierId(supplierId);
        transaction.setReferenceNumber(referenceNumber);
        transaction.setRemarks(remarks);
        transaction.setCreatedBy(createdBy);
        
        InventoryTransaction savedTransaction = inventoryTransactionRepository.save(transaction);
        
        // Update inventory quantity
        inventoryService.addInventoryQuantity(branchId, productId, quantity, 
                "Purchase - " + referenceNumber, createdBy);
        
        return convertToDTO(savedTransaction);
    }
    
    public InventoryTransactionDTO createAdjustmentTransaction(Long branchId, Long productId, 
                                                            BigDecimal quantity, String remarks, Long createdBy) {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setBranchId(branchId);
        transaction.setProductId(productId);
        transaction.setType(InventoryTransaction.TransactionType.ADJUSTMENT);
        transaction.setQuantity(quantity);
        transaction.setRemarks(remarks);
        transaction.setCreatedBy(createdBy);
        
        InventoryTransaction savedTransaction = inventoryTransactionRepository.save(transaction);
        return convertToDTO(savedTransaction);
    }
    
    public InventoryTransactionDTO createTransferTransaction(Long fromBranchId, Long toBranchId, 
                                                          Long productId, BigDecimal quantity, 
                                                          String remarks, Long createdBy) {
        // Create deduction transaction for source branch
        InventoryTransaction deductionTransaction = new InventoryTransaction();
        deductionTransaction.setBranchId(fromBranchId);
        deductionTransaction.setProductId(productId);
        deductionTransaction.setType(InventoryTransaction.TransactionType.TRANSFER);
        deductionTransaction.setQuantity(quantity.negate());
        deductionTransaction.setRemarks("Transfer to branch " + toBranchId + " - " + remarks);
        deductionTransaction.setCreatedBy(createdBy);
        inventoryTransactionRepository.save(deductionTransaction);
        
        // Create addition transaction for destination branch
        InventoryTransaction additionTransaction = new InventoryTransaction();
        additionTransaction.setBranchId(toBranchId);
        additionTransaction.setProductId(productId);
        additionTransaction.setType(InventoryTransaction.TransactionType.TRANSFER);
        additionTransaction.setQuantity(quantity);
        additionTransaction.setRemarks("Transfer from branch " + fromBranchId + " - " + remarks);
        additionTransaction.setCreatedBy(createdBy);
        InventoryTransaction savedTransaction = inventoryTransactionRepository.save(additionTransaction);
        
        // Update inventory quantities
        inventoryService.deductInventoryQuantity(fromBranchId, productId, quantity, 
                "Transfer to branch " + toBranchId, createdBy);
        inventoryService.addInventoryQuantity(toBranchId, productId, quantity, 
                "Transfer from branch " + fromBranchId, createdBy);
        
        return convertToDTO(savedTransaction);
    }
    
    public InventoryTransactionDTO createUsageTransaction(Long branchId, Long productId, 
                                                       BigDecimal quantity, String remarks, Long createdBy) {
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setBranchId(branchId);
        transaction.setProductId(productId);
        transaction.setType(InventoryTransaction.TransactionType.USAGE);
        transaction.setQuantity(quantity.negate()); // Usage is negative quantity
        transaction.setRemarks(remarks);
        transaction.setCreatedBy(createdBy);
        
        InventoryTransaction savedTransaction = inventoryTransactionRepository.save(transaction);
        return convertToDTO(savedTransaction);
    }
    
    public PurchaseEntryDTO processPurchaseEntry(PurchaseEntryDTO purchaseEntry) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // Process each item in the purchase
        for (PurchaseEntryDTO.PurchaseItemDTO item : purchaseEntry.getItems()) {
            BigDecimal itemTotal = item.getQuantity().multiply(item.getUnitPrice());
            item.setTotalAmount(itemTotal);
            totalAmount = totalAmount.add(itemTotal);
            
            // Create purchase transaction for each item
            createPurchaseTransaction(
                    purchaseEntry.getBranchId(),
                    item.getProductId(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getTotalAmount(),
                    purchaseEntry.getSupplierId(),
                    purchaseEntry.getReferenceNumber(),
                    purchaseEntry.getRemarks(),
                    purchaseEntry.getCreatedBy()
            );
        }
        
        purchaseEntry.setTotalAmount(totalAmount);
        purchaseEntry.setCreatedAt(LocalDateTime.now());
        
        return purchaseEntry;
    }
    
    @Transactional(readOnly = true)
    public Page<InventoryTransactionDTO> getTransactionsByFilters(Long branchId, Long productId, 
                                                               String type, Long supplierId, 
                                                               LocalDateTime fromDate, LocalDateTime toDate, 
                                                               Pageable pageable) {
        InventoryTransaction.TransactionType typeEnum = type != null ? 
                InventoryTransaction.TransactionType.valueOf(type) : null;
        
        return inventoryTransactionRepository.findByFilters(branchId, productId, typeEnum, supplierId, 
                                                          fromDate, toDate, pageable)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public List<InventoryTransactionDTO> getTransactionsByBranchAndDateRange(Long branchId, 
                                                                           LocalDateTime fromDate, 
                                                                           LocalDateTime toDate) {
        return inventoryTransactionRepository.findByBranchIdAndDateRange(branchId, fromDate, toDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InventoryTransactionDTO> getRecentTransactionsByBranch(Long branchId, Pageable pageable) {
        return inventoryTransactionRepository.findRecentTransactionsByBranch(branchId, pageable)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalPurchaseAmount(Long branchId, LocalDateTime fromDate, LocalDateTime toDate) {
        return inventoryTransactionRepository.calculateTotalPurchaseAmount(branchId, fromDate, toDate);
    }
    
    @Transactional(readOnly = true)
    public Long getPurchaseCount(Long branchId, LocalDateTime fromDate, LocalDateTime toDate) {
        return inventoryTransactionRepository.countPurchasesByBranchAndDateRange(branchId, fromDate, toDate);
    }
    
    // Helper methods
    private InventoryTransactionDTO convertToDTO(InventoryTransaction transaction) {
        InventoryTransactionDTO dto = new InventoryTransactionDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setBranchId(transaction.getBranchId());
        dto.setProductId(transaction.getProductId());
        dto.setType(transaction.getType().name());
        dto.setQuantity(transaction.getQuantity());
        dto.setUnitPrice(transaction.getUnitPrice());
        dto.setTotalAmount(transaction.getTotalAmount());
        dto.setSupplierId(transaction.getSupplierId());
        dto.setReferenceNumber(transaction.getReferenceNumber());
        dto.setRemarks(transaction.getRemarks());
        dto.setCreatedBy(transaction.getCreatedBy());
        dto.setCreatedAt(transaction.getCreatedAt());
        
        // Set additional fields if product is loaded
        if (transaction.getProduct() != null) {
            dto.setProductName(transaction.getProduct().getName());
            dto.setProductCode(transaction.getProduct().getCode());
        }
        
        // Set additional fields if branch is loaded
        if (transaction.getBranch() != null) {
            dto.setBranchName(transaction.getBranch().getBranchName());
        }
        
        // Set additional fields if supplier is loaded
        if (transaction.getSupplier() != null) {
            dto.setSupplierName(transaction.getSupplier().getName());
        }
        
        return dto;
    }
}
