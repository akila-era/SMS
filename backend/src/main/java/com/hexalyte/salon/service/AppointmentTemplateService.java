package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.AppointmentDTO;
import com.hexalyte.salon.dto.AppointmentServiceDTO;
import com.hexalyte.salon.dto.AppointmentTemplateDTO;
import com.hexalyte.salon.dto.AppointmentTemplateServiceDTO;
import com.hexalyte.salon.model.*;
import com.hexalyte.salon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentTemplateService {

    @Autowired
    private AppointmentTemplateRepository templateRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private AppointmentService appointmentService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Create template
    public AppointmentTemplateDTO createTemplate(AppointmentTemplateDTO templateDTO) {
        Branch branch = branchRepository.findById(templateDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        AppointmentTemplate template = new AppointmentTemplate();
        template.setTemplateName(templateDTO.getTemplateName());
        template.setDescription(templateDTO.getDescription());
        template.setBranch(branch);
        template.setEstimatedDurationMinutes(templateDTO.getEstimatedDurationMinutes());
        template.setEstimatedPrice(templateDTO.getEstimatedPrice());
        template.setIsActive(templateDTO.getIsActive());

        // Calculate estimated duration and price from services
        if (templateDTO.getTemplateServices() != null && !templateDTO.getTemplateServices().isEmpty()) {
            int totalDuration = 0;
            BigDecimal totalPrice = BigDecimal.ZERO;

            for (AppointmentTemplateServiceDTO serviceDTO : templateDTO.getTemplateServices()) {
                com.hexalyte.salon.model.Service service = serviceRepository.findById(serviceDTO.getServiceId())
                        .orElseThrow(() -> new RuntimeException("Service not found: " + serviceDTO.getServiceId()));

                com.hexalyte.salon.model.AppointmentTemplateService templateService = new com.hexalyte.salon.model.AppointmentTemplateService();
                templateService.setTemplate(template);
                templateService.setService(service);
                templateService.setPrice(serviceDTO.getPrice());
                templateService.setCommissionRate(serviceDTO.getCommissionRate());
                templateService.setServiceOrder(serviceDTO.getServiceOrder());

                template.getTemplateServices().add(templateService);
                totalDuration += service.getDurationMinutes();
                totalPrice = totalPrice.add(serviceDTO.getPrice());
            }

            template.setEstimatedDurationMinutes(totalDuration);
            template.setEstimatedPrice(totalPrice);
        }

        AppointmentTemplate savedTemplate = templateRepository.save(template);
        return convertToDTO(savedTemplate);
    }

    // Get all templates
    public List<AppointmentTemplateDTO> getAllTemplates() {
        return templateRepository.findByIsActiveTrueOrderByUsageCountDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get templates by branch
    public List<AppointmentTemplateDTO> getTemplatesByBranch(Long branchId) {
        return templateRepository.findByBranchIdAndIsActiveTrueOrderByUsageCountDesc(branchId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get template by ID
    public AppointmentTemplateDTO getTemplateById(Long templateId) {
        AppointmentTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        return convertToDTO(template);
    }

    // Search templates
    public List<AppointmentTemplateDTO> searchTemplates(String searchTerm, Long branchId) {
        List<AppointmentTemplate> templates;
        if (branchId != null) {
            templates = templateRepository.findByBranchIdAndTemplateNameContainingIgnoreCaseAndIsActiveTrue(
                    branchId, searchTerm);
        } else {
            templates = templateRepository.findByTemplateNameContainingIgnoreCaseAndIsActiveTrue(searchTerm);
        }
        
        return templates.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Create appointment from template
    public AppointmentDTO createAppointmentFromTemplate(Long templateId, Long customerId, Long staffId, 
                                                      LocalDate appointmentDate, LocalTime startTime) {
        AppointmentTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        if (!template.getIsActive()) {
            throw new RuntimeException("Template is not active");
        }

        // Create appointment DTO from template
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setCustomerId(customerId);
        appointmentDTO.setStaffId(staffId);
        appointmentDTO.setBranchId(template.getBranch().getId());
        appointmentDTO.setAppointmentDate(appointmentDate);
        appointmentDTO.setStartTime(startTime);
        appointmentDTO.setNotes("Created from template: " + template.getTemplateName());

        // Convert template services to appointment services
        List<AppointmentServiceDTO> appointmentServices = new ArrayList<>();
        for (com.hexalyte.salon.model.AppointmentTemplateService templateService : template.getTemplateServices()) {
            AppointmentServiceDTO serviceDTO = new AppointmentServiceDTO();
            serviceDTO.setServiceId(templateService.getService().getId());
            serviceDTO.setPrice(templateService.getPrice());
            serviceDTO.setCommissionRate(templateService.getCommissionRate());
            appointmentServices.add(serviceDTO);
        }
        appointmentDTO.setServices(appointmentServices);

        // Create appointment
        AppointmentDTO createdAppointment = appointmentService.createAppointment(appointmentDTO);

        // Increment usage count
        template.setUsageCount(template.getUsageCount() + 1);
        templateRepository.save(template);

        return createdAppointment;
    }

    // Update template
    public AppointmentTemplateDTO updateTemplate(Long templateId, AppointmentTemplateDTO templateDTO) {
        AppointmentTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        template.setTemplateName(templateDTO.getTemplateName());
        template.setDescription(templateDTO.getDescription());
        template.setIsActive(templateDTO.getIsActive());

        // Update services if provided
        if (templateDTO.getTemplateServices() != null) {
            template.getTemplateServices().clear();
            
            int totalDuration = 0;
            BigDecimal totalPrice = BigDecimal.ZERO;

            for (AppointmentTemplateServiceDTO serviceDTO : templateDTO.getTemplateServices()) {
                com.hexalyte.salon.model.Service service = serviceRepository.findById(serviceDTO.getServiceId())
                        .orElseThrow(() -> new RuntimeException("Service not found: " + serviceDTO.getServiceId()));

                com.hexalyte.salon.model.AppointmentTemplateService templateService = new com.hexalyte.salon.model.AppointmentTemplateService();
                templateService.setTemplate(template);
                templateService.setService(service);
                templateService.setPrice(serviceDTO.getPrice());
                templateService.setCommissionRate(serviceDTO.getCommissionRate());
                templateService.setServiceOrder(serviceDTO.getServiceOrder());

                template.getTemplateServices().add(templateService);
                totalDuration += service.getDurationMinutes();
                totalPrice = totalPrice.add(serviceDTO.getPrice());
            }

            template.setEstimatedDurationMinutes(totalDuration);
            template.setEstimatedPrice(totalPrice);
        }

        AppointmentTemplate savedTemplate = templateRepository.save(template);
        return convertToDTO(savedTemplate);
    }

    // Delete template
    public void deleteTemplate(Long templateId) {
        AppointmentTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        
        template.setIsActive(false);
        templateRepository.save(template);
    }

    // Get most popular templates
    public List<AppointmentTemplateDTO> getMostPopularTemplates(Long branchId) {
        List<AppointmentTemplate> templates;
        if (branchId != null) {
            templates = templateRepository.findMostPopularTemplatesByBranch(branchId);
        } else {
            templates = templateRepository.findMostPopularTemplates();
        }
        
        return templates.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AppointmentTemplateDTO convertToDTO(AppointmentTemplate template) {
        AppointmentTemplateDTO dto = new AppointmentTemplateDTO();
        dto.setId(template.getId());
        dto.setTemplateName(template.getTemplateName());
        dto.setDescription(template.getDescription());
        dto.setBranchId(template.getBranch().getId());
        dto.setBranchName(template.getBranch().getName());
        dto.setEstimatedDurationMinutes(template.getEstimatedDurationMinutes());
        dto.setEstimatedPrice(template.getEstimatedPrice());
        dto.setIsActive(template.getIsActive());
        dto.setUsageCount(template.getUsageCount());

        if (template.getCreatedAt() != null) {
            dto.setCreatedAt(template.getCreatedAt().format(formatter));
        }
        if (template.getUpdatedAt() != null) {
            dto.setUpdatedAt(template.getUpdatedAt().format(formatter));
        }

        // Convert template services
        List<AppointmentTemplateServiceDTO> serviceDTOs = template.getTemplateServices().stream()
                .map(this::convertTemplateServiceToDTO)
                .collect(Collectors.toList());
        dto.setTemplateServices(serviceDTOs);

        return dto;
    }

    private AppointmentTemplateServiceDTO convertTemplateServiceToDTO(com.hexalyte.salon.model.AppointmentTemplateService templateService) {
        AppointmentTemplateServiceDTO dto = new AppointmentTemplateServiceDTO();
        dto.setId(templateService.getId());
        dto.setServiceId(templateService.getService().getId());
        dto.setServiceName(templateService.getService().getName());
        dto.setServiceCategory(templateService.getService().getCategory());
        dto.setPrice(templateService.getPrice());
        dto.setCommissionRate(templateService.getCommissionRate());
        dto.setServiceOrder(templateService.getServiceOrder());
        dto.setDurationMinutes(templateService.getService().getDurationMinutes());
        return dto;
    }
}
