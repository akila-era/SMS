package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.ServiceDTO;
import com.hexalyte.salon.model.Service;
import com.hexalyte.salon.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ServiceDTO> getActiveServices() {
        return serviceRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ServiceDTO> getServicesByCategory(String category) {
        return serviceRepository.findByCategoryAndIsActiveTrue(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<String> getCategories() {
        return serviceRepository.findDistinctCategories();
    }

    public List<ServiceDTO> searchServices(String name) {
        return serviceRepository.findByNameContaining(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ServiceDTO getServiceById(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        return convertToDTO(service);
    }

    public ServiceDTO createService(ServiceDTO serviceDTO) {
        Service service = new Service();
        service.setName(serviceDTO.getName());
        service.setDescription(serviceDTO.getDescription());
        service.setPrice(serviceDTO.getPrice());
        service.setDurationMinutes(serviceDTO.getDurationMinutes());
        service.setCommissionRate(serviceDTO.getCommissionRate() != null ? serviceDTO.getCommissionRate() : java.math.BigDecimal.ZERO);
        service.setCategory(serviceDTO.getCategory());
        service.setIsActive(serviceDTO.getIsActive());

        Service savedService = serviceRepository.save(service);
        return convertToDTO(savedService);
    }

    public ServiceDTO updateService(Long id, ServiceDTO serviceDTO) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));

        service.setName(serviceDTO.getName());
        service.setDescription(serviceDTO.getDescription());
        service.setPrice(serviceDTO.getPrice());
        service.setDurationMinutes(serviceDTO.getDurationMinutes());
        service.setCommissionRate(serviceDTO.getCommissionRate() != null ? serviceDTO.getCommissionRate() : java.math.BigDecimal.ZERO);
        service.setCategory(serviceDTO.getCategory());
        service.setIsActive(serviceDTO.getIsActive());

        Service savedService = serviceRepository.save(service);
        return convertToDTO(savedService);
    }

    public void deleteService(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        
        // Check if service has appointments
        if (!service.getAppointmentServices().isEmpty()) {
            throw new RuntimeException("Cannot delete service with existing appointments. Please handle appointments first.");
        }

        serviceRepository.delete(service);
    }

    public ServiceDTO toggleServiceStatus(Long id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        
        service.setIsActive(!service.getIsActive());
        Service savedService = serviceRepository.save(service);
        return convertToDTO(savedService);
    }

    private ServiceDTO convertToDTO(Service service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setPrice(service.getPrice());
        dto.setDurationMinutes(service.getDurationMinutes());
        dto.setCommissionRate(service.getCommissionRate());
        dto.setCategory(service.getCategory());
        dto.setIsActive(service.getIsActive());
        
        if (service.getCreatedAt() != null) {
            dto.setCreatedAt(service.getCreatedAt().format(formatter));
        }
        
        if (service.getUpdatedAt() != null) {
            dto.setUpdatedAt(service.getUpdatedAt().format(formatter));
        }
        
        dto.setAppointmentCount(service.getAppointmentServices().size());
        
        return dto;
    }
}
