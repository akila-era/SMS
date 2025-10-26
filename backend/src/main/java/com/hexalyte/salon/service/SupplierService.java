package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.SupplierDTO;
import com.hexalyte.salon.model.Supplier;
import com.hexalyte.salon.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierService {
    
    @Autowired
    private SupplierRepository supplierRepository;
    
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        // Check if email already exists
        if (supplierDTO.getEmail() != null && supplierRepository.existsByEmail(supplierDTO.getEmail())) {
            throw new IllegalArgumentException("Supplier email already exists: " + supplierDTO.getEmail());
        }
        
        Supplier supplier = convertToEntity(supplierDTO);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return convertToDTO(savedSupplier);
    }
    
    public SupplierDTO updateSupplier(Long supplierId, SupplierDTO supplierDTO) {
        Supplier existingSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + supplierId));
        
        // Check if email already exists for different supplier
        if (supplierDTO.getEmail() != null && 
            supplierRepository.existsByEmailAndSupplierIdNot(supplierDTO.getEmail(), supplierId)) {
            throw new IllegalArgumentException("Supplier email already exists: " + supplierDTO.getEmail());
        }
        
        // Update fields
        existingSupplier.setName(supplierDTO.getName());
        existingSupplier.setContactNumber(supplierDTO.getContactNumber());
        existingSupplier.setEmail(supplierDTO.getEmail());
        existingSupplier.setAddress(supplierDTO.getAddress());
        existingSupplier.setPaymentTerms(supplierDTO.getPaymentTerms());
        existingSupplier.setStatus(Supplier.SupplierStatus.valueOf(supplierDTO.getStatus()));
        
        Supplier savedSupplier = supplierRepository.save(existingSupplier);
        return convertToDTO(savedSupplier);
    }
    
    @Transactional(readOnly = true)
    public SupplierDTO getSupplierById(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + supplierId));
        return convertToDTO(supplier);
    }
    
    @Transactional(readOnly = true)
    public Page<SupplierDTO> getAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<SupplierDTO> getSuppliersByFilters(String name, String status, Pageable pageable) {
        Supplier.SupplierStatus statusEnum = status != null ? Supplier.SupplierStatus.valueOf(status) : null;
        
        return supplierRepository.findByFilters(name, statusEnum, pageable)
                .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public List<SupplierDTO> getActiveSuppliers() {
        return supplierRepository.findActiveSuppliers()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public void deleteSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + supplierId));
        
        // Check if supplier is being used in transactions
        // This would require checking inventory transactions
        // For now, we'll just deactivate the supplier
        supplier.setStatus(Supplier.SupplierStatus.INACTIVE);
        supplierRepository.save(supplier);
    }
    
    public void activateSupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found with id: " + supplierId));
        
        supplier.setStatus(Supplier.SupplierStatus.ACTIVE);
        supplierRepository.save(supplier);
    }
    
    // Helper methods
    private Supplier convertToEntity(SupplierDTO dto) {
        Supplier supplier = new Supplier();
        supplier.setName(dto.getName());
        supplier.setContactNumber(dto.getContactNumber());
        supplier.setEmail(dto.getEmail());
        supplier.setAddress(dto.getAddress());
        supplier.setPaymentTerms(dto.getPaymentTerms());
        supplier.setStatus(Supplier.SupplierStatus.valueOf(dto.getStatus()));
        return supplier;
    }
    
    private SupplierDTO convertToDTO(Supplier supplier) {
        SupplierDTO dto = new SupplierDTO();
        dto.setSupplierId(supplier.getSupplierId());
        dto.setName(supplier.getName());
        dto.setContactNumber(supplier.getContactNumber());
        dto.setEmail(supplier.getEmail());
        dto.setAddress(supplier.getAddress());
        dto.setPaymentTerms(supplier.getPaymentTerms());
        dto.setStatus(supplier.getStatus().name());
        dto.setCreatedAt(supplier.getCreatedAt());
        dto.setUpdatedAt(supplier.getUpdatedAt());
        return dto;
    }
}
