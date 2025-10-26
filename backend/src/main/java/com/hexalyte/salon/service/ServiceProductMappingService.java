package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.ServiceProductMappingDTO;
import com.hexalyte.salon.model.ServiceProductMapping;
import com.hexalyte.salon.repository.ServiceProductMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceProductMappingService {
    
    @Autowired
    private ServiceProductMappingRepository serviceProductMappingRepository;
    
    public ServiceProductMappingDTO createMapping(ServiceProductMappingDTO mappingDTO) {
        // Check if mapping already exists
        if (serviceProductMappingRepository.findByServiceIdAndProductId(
                mappingDTO.getServiceId(), mappingDTO.getProductId()).isPresent()) {
            throw new IllegalArgumentException("Service-Product mapping already exists");
        }
        
        ServiceProductMapping mapping = convertToEntity(mappingDTO);
        ServiceProductMapping savedMapping = serviceProductMappingRepository.save(mapping);
        return convertToDTO(savedMapping);
    }
    
    public ServiceProductMappingDTO updateMapping(Long mappingId, ServiceProductMappingDTO mappingDTO) {
        ServiceProductMapping existingMapping = serviceProductMappingRepository.findById(mappingId)
                .orElseThrow(() -> new IllegalArgumentException("Service-Product mapping not found with id: " + mappingId));
        
        // Check if mapping already exists for different mapping
        Optional<ServiceProductMapping> existingMappingCheck = serviceProductMappingRepository
                .findByServiceIdAndProductId(mappingDTO.getServiceId(), mappingDTO.getProductId());
        
        if (existingMappingCheck.isPresent() && !existingMappingCheck.get().getId().equals(mappingId)) {
            throw new IllegalArgumentException("Service-Product mapping already exists");
        }
        
        // Update fields
        existingMapping.setServiceId(mappingDTO.getServiceId());
        existingMapping.setProductId(mappingDTO.getProductId());
        existingMapping.setDefaultQuantity(mappingDTO.getDefaultQuantity());
        existingMapping.setIsRequired(mappingDTO.getIsRequired());
        
        ServiceProductMapping savedMapping = serviceProductMappingRepository.save(existingMapping);
        return convertToDTO(savedMapping);
    }
    
    @Transactional(readOnly = true)
    public ServiceProductMappingDTO getMappingById(Long mappingId) {
        ServiceProductMapping mapping = serviceProductMappingRepository.findById(mappingId)
                .orElseThrow(() -> new IllegalArgumentException("Service-Product mapping not found with id: " + mappingId));
        return convertToDTO(mapping);
    }
    
    @Transactional(readOnly = true)
    public List<ServiceProductMappingDTO> getMappingsByService(Long serviceId) {
        return serviceProductMappingRepository.findByServiceIdWithProduct(serviceId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ServiceProductMappingDTO> getMappingsByProduct(Long productId) {
        return serviceProductMappingRepository.findByProductIdWithService(productId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ServiceProductMappingDTO> getMappingsByServices(List<Long> serviceIds) {
        return serviceProductMappingRepository.findByServiceIdIn(serviceIds)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public void deleteMapping(Long mappingId) {
        ServiceProductMapping mapping = serviceProductMappingRepository.findById(mappingId)
                .orElseThrow(() -> new IllegalArgumentException("Service-Product mapping not found with id: " + mappingId));
        
        serviceProductMappingRepository.delete(mapping);
    }
    
    public void deleteMappingsByService(Long serviceId) {
        serviceProductMappingRepository.deleteByServiceId(serviceId);
    }
    
    public void deleteMappingsByProduct(Long productId) {
        serviceProductMappingRepository.deleteByProductId(productId);
    }
    
    public void deleteMappingByServiceAndProduct(Long serviceId, Long productId) {
        serviceProductMappingRepository.deleteByServiceIdAndProductId(serviceId, productId);
    }
    
    // Helper methods
    private ServiceProductMapping convertToEntity(ServiceProductMappingDTO dto) {
        ServiceProductMapping mapping = new ServiceProductMapping();
        mapping.setServiceId(dto.getServiceId());
        mapping.setProductId(dto.getProductId());
        mapping.setDefaultQuantity(dto.getDefaultQuantity());
        mapping.setIsRequired(dto.getIsRequired());
        return mapping;
    }
    
    private ServiceProductMappingDTO convertToDTO(ServiceProductMapping mapping) {
        ServiceProductMappingDTO dto = new ServiceProductMappingDTO();
        dto.setId(mapping.getId());
        dto.setServiceId(mapping.getServiceId());
        dto.setProductId(mapping.getProductId());
        dto.setDefaultQuantity(mapping.getDefaultQuantity());
        dto.setIsRequired(mapping.getIsRequired());
        dto.setCreatedAt(mapping.getCreatedAt());
        
        // Set additional fields if service is loaded
        if (mapping.getService() != null) {
            dto.setServiceName(mapping.getService().getName());
        }
        
        // Set additional fields if product is loaded
        if (mapping.getProduct() != null) {
            dto.setProductName(mapping.getProduct().getName());
            dto.setProductCode(mapping.getProduct().getCode());
            dto.setProductUom(mapping.getProduct().getUom());
            dto.setProductType(mapping.getProduct().getProductType().name());
        }
        
        return dto;
    }
}
