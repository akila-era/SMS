package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.InventoryUsageDTO;
import com.hexalyte.salon.dto.InventoryDashboardDTO;
import com.hexalyte.salon.dto.ServiceProductMappingDTO;
import com.hexalyte.salon.model.InventoryUsage;
import com.hexalyte.salon.model.Appointment;
import com.hexalyte.salon.model.AppointmentService;
import com.hexalyte.salon.model.Product;
import com.hexalyte.salon.model.ServiceProductMapping;
import com.hexalyte.salon.repository.InventoryUsageRepository;
import com.hexalyte.salon.repository.AppointmentRepository;
import com.hexalyte.salon.repository.ProductRepository;
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
public class InventoryUsageService {
    
    @Autowired
    private InventoryUsageRepository inventoryUsageRepository;
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private InventoryTransactionService inventoryTransactionService;
    
    @Autowired
    private ServiceProductMappingService serviceProductMappingService;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    public InventoryUsageDTO recordProductUsage(Long appointmentId, Long serviceId, Long staffId, 
                                              Long branchId, Long productId, BigDecimal quantityUsed, 
                                              BigDecimal unitCost) {
        BigDecimal totalCost = quantityUsed.multiply(unitCost);
        
        InventoryUsage usage = new InventoryUsage();
        usage.setAppointmentId(appointmentId);
        usage.setServiceId(serviceId);
        usage.setStaffId(staffId);
        usage.setBranchId(branchId);
        usage.setProductId(productId);
        usage.setQuantityUsed(quantityUsed);
        usage.setUnitCost(unitCost);
        usage.setTotalCost(totalCost);
        
        InventoryUsage savedUsage = inventoryUsageRepository.save(usage);
        
        // Deduct from inventory
        inventoryService.deductInventoryQuantity(branchId, productId, quantityUsed, 
                "Used in appointment " + appointmentId, staffId);
        
        // Create usage transaction
        inventoryTransactionService.createUsageTransaction(branchId, productId, quantityUsed, 
                "Used in appointment " + appointmentId, staffId);
        
        return convertToDTO(savedUsage);
    }
    
    @Transactional(readOnly = true)
    public List<InventoryUsageDTO> getUsageByAppointment(Long appointmentId) {
        return inventoryUsageRepository.findByAppointmentId(appointmentId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InventoryUsageDTO> getUsageByService(Long serviceId) {
        return inventoryUsageRepository.findByServiceId(serviceId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InventoryUsageDTO> getUsageByStaff(Long staffId) {
        return inventoryUsageRepository.findByStaffId(staffId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InventoryUsageDTO> getUsageByBranch(Long branchId) {
        return inventoryUsageRepository.findByBranchId(branchId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InventoryUsageDTO> getUsageByProduct(Long productId) {
        return inventoryUsageRepository.findByProductId(productId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<InventoryUsageDTO> getUsageByFilters(Long branchId, Long productId, Long serviceId, 
                                                   Long staffId, LocalDateTime fromDate, LocalDateTime toDate, 
                                                   Pageable pageable) {
        return inventoryUsageRepository.findByFilters(branchId, productId, serviceId, staffId, 
                                                    fromDate, toDate, pageable)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public List<InventoryUsageDTO> getUsageByBranchAndDateRange(Long branchId, LocalDateTime fromDate, 
                                                              LocalDateTime toDate) {
        return inventoryUsageRepository.findByBranchIdAndDateRange(branchId, fromDate, toDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<InventoryUsageDTO> getRecentUsageByBranch(Long branchId, Pageable pageable) {
        return inventoryUsageRepository.findRecentUsageByBranch(branchId, pageable)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalUsageCost(Long branchId, LocalDateTime fromDate, LocalDateTime toDate) {
        return inventoryUsageRepository.calculateTotalUsageCost(branchId, fromDate, toDate);
    }
    
    @Transactional(readOnly = true)
    public List<InventoryDashboardDTO.ProductUsageSummaryDTO> getTopUsedProducts(Long branchId, 
                                                                               LocalDateTime fromDate, 
                                                                               LocalDateTime toDate, 
                                                                               Pageable pageable) {
        List<Object[]> results = branchId != null ? 
                inventoryUsageRepository.findTopUsedProductsByBranchAndDateRange(branchId, fromDate, toDate, pageable) :
                inventoryUsageRepository.findTopUsedProductsByDateRange(fromDate, toDate, pageable);
        
        return results.stream()
                .map(this::convertToProductUsageSummary)
                .collect(Collectors.toList());
    }
    
    public void trackInventoryUsageForAppointment(Long appointmentId) {
        try {
            // Get appointment details
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id: " + appointmentId));
            
            // Get all services for this appointment
            List<com.hexalyte.salon.model.AppointmentService> appointmentServices = appointment.getAppointmentServices();
            
            for (com.hexalyte.salon.model.AppointmentService appointmentService : appointmentServices) {
                Long serviceId = appointmentService.getService().getId();
                
                // Get product mappings for this service
                List<com.hexalyte.salon.dto.ServiceProductMappingDTO> mappings = serviceProductMappingService.getMappingsByService(serviceId);
                
                for (com.hexalyte.salon.dto.ServiceProductMappingDTO mapping : mappings) {
                    Long productId = mapping.getProductId();
                    BigDecimal defaultQuantity = mapping.getDefaultQuantity();
                    
                    // Get product details
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
                    
                    // Record inventory usage
                    recordProductUsage(
                            appointmentId,
                            serviceId,
                            appointment.getStaff().getId(),
                            appointment.getBranch().getId(),
                            productId,
                            defaultQuantity,
                            product.getCostPrice()
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Error tracking inventory usage for appointment " + appointmentId + ": " + e.getMessage());
            throw e;
        }
    }
    
    // Helper methods
    private InventoryUsageDTO convertToDTO(InventoryUsage usage) {
        InventoryUsageDTO dto = new InventoryUsageDTO();
        dto.setId(usage.getId());
        dto.setAppointmentId(usage.getAppointmentId());
        dto.setServiceId(usage.getServiceId());
        dto.setStaffId(usage.getStaffId());
        dto.setBranchId(usage.getBranchId());
        dto.setProductId(usage.getProductId());
        dto.setQuantityUsed(usage.getQuantityUsed());
        dto.setUnitCost(usage.getUnitCost());
        dto.setTotalCost(usage.getTotalCost());
        dto.setUsedAt(usage.getUsedAt());
        
        // Set additional fields if product is loaded
        if (usage.getProduct() != null) {
            dto.setProductName(usage.getProduct().getName());
            dto.setProductCode(usage.getProduct().getCode());
            dto.setProductUom(usage.getProduct().getUom());
        }
        
        // Set additional fields if service is loaded
        if (usage.getService() != null) {
            dto.setServiceName(usage.getService().getName());
        }
        
        // Set additional fields if staff is loaded
        if (usage.getStaff() != null) {
            dto.setStaffName(usage.getStaff().getFirstName() + " " + usage.getStaff().getLastName());
        }
        
        // Set additional fields if branch is loaded
        if (usage.getBranch() != null) {
            dto.setBranchName(usage.getBranch().getBranchName());
        }
        
        // Set additional fields if appointment is loaded
        if (usage.getAppointment() != null) {
            dto.setCustomerName(usage.getAppointment().getCustomer().getFirstName() + " " + 
                              usage.getAppointment().getCustomer().getLastName());
            dto.setAppointmentDate(usage.getAppointment().getAppointmentDate().toString());
            dto.setAppointmentTime(usage.getAppointment().getStartTime().toString());
        }
        
        return dto;
    }
    
    private InventoryDashboardDTO.ProductUsageSummaryDTO convertToProductUsageSummary(Object[] result) {
        Long productId = (Long) result[0];
        BigDecimal totalQuantityUsed = (BigDecimal) result[1];
        BigDecimal totalCost = (BigDecimal) result[2];
        Long usageCount = (Long) result[3];
        
        InventoryDashboardDTO.ProductUsageSummaryDTO summary = new InventoryDashboardDTO.ProductUsageSummaryDTO();
        summary.setProductId(productId);
        summary.setTotalQuantityUsed(totalQuantityUsed);
        summary.setTotalCost(totalCost);
        summary.setUsageCount(usageCount);
        
        return summary;
    }
}
