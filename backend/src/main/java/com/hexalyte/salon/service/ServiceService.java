package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.*;
import com.hexalyte.salon.model.*;
import com.hexalyte.salon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceBranchConfigurationRepository branchConfigRepository;

    @Autowired
    private ServicePackageRepository packageRepository;

    @Autowired
    private ServicePackageItemRepository packageItemRepository;

    @Autowired
    private ServicePromotionRepository promotionRepository;

    @Autowired
    private ServiceProductUsageRepository productUsageRepository;

    @Autowired
    private ServiceAnalyticsRepository analyticsRepository;

    @Autowired
    private PromotionUsageRepository promotionUsageRepository;

    @Autowired
    private BranchRepository branchRepository;

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
        
        // New advanced fields
        service.setPreparationBufferMinutes(serviceDTO.getPreparationBufferMinutes());
        service.setCleanupBufferMinutes(serviceDTO.getCleanupBufferMinutes());
        service.setDefaultProducts(serviceDTO.getDefaultProducts());
        service.setIsTaxable(serviceDTO.getIsTaxable());
        service.setRequiredSkills(serviceDTO.getRequiredSkills());
        service.setResourceRequirements(serviceDTO.getResourceRequirements());
        service.setCommissionType(serviceDTO.getCommissionType());
        service.setFixedCommissionAmount(serviceDTO.getFixedCommissionAmount());

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
        
        // New advanced fields
        service.setPreparationBufferMinutes(serviceDTO.getPreparationBufferMinutes());
        service.setCleanupBufferMinutes(serviceDTO.getCleanupBufferMinutes());
        service.setDefaultProducts(serviceDTO.getDefaultProducts());
        service.setIsTaxable(serviceDTO.getIsTaxable());
        service.setRequiredSkills(serviceDTO.getRequiredSkills());
        service.setResourceRequirements(serviceDTO.getResourceRequirements());
        service.setCommissionType(serviceDTO.getCommissionType());
        service.setFixedCommissionAmount(serviceDTO.getFixedCommissionAmount());

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
        
        // New advanced fields
        dto.setPreparationBufferMinutes(service.getPreparationBufferMinutes());
        dto.setCleanupBufferMinutes(service.getCleanupBufferMinutes());
        dto.setDefaultProducts(service.getDefaultProducts());
        dto.setIsTaxable(service.getIsTaxable());
        dto.setRequiredSkills(service.getRequiredSkills());
        dto.setResourceRequirements(service.getResourceRequirements());
        dto.setCommissionType(service.getCommissionType());
        dto.setFixedCommissionAmount(service.getFixedCommissionAmount());
        dto.setTotalDurationMinutes(service.getTotalDurationMinutes());
        
        if (service.getCreatedAt() != null) {
            dto.setCreatedAt(service.getCreatedAt().format(formatter));
        }
        
        if (service.getUpdatedAt() != null) {
            dto.setUpdatedAt(service.getUpdatedAt().format(formatter));
        }
        
        dto.setAppointmentCount(service.getAppointmentServices().size());
        
        return dto;
    }

    // ========== BRANCH-SPECIFIC CONFIGURATION METHODS ==========

    public ServiceBranchConfigurationDTO createBranchConfiguration(Long serviceId, Long branchId, ServiceBranchConfigurationDTO configDTO) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));
        
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + branchId));

        ServiceBranchConfiguration config = new ServiceBranchConfiguration();
        config.setService(service);
        config.setBranch(branch);
        config.setPrice(configDTO.getPrice());
        config.setDurationMinutes(configDTO.getDurationMinutes());
        config.setCommissionRate(configDTO.getCommissionRate());
        config.setCommissionType(configDTO.getCommissionType());
        config.setFixedCommissionAmount(configDTO.getFixedCommissionAmount());
        config.setIsEnabled(configDTO.getIsEnabled());
        config.setRequiredSkills(configDTO.getRequiredSkills());
        config.setResourceRequirements(configDTO.getResourceRequirements());

        ServiceBranchConfiguration savedConfig = branchConfigRepository.save(config);
        return convertBranchConfigToDTO(savedConfig);
    }

    public ServiceBranchConfigurationDTO updateBranchConfiguration(Long configId, ServiceBranchConfigurationDTO configDTO) {
        ServiceBranchConfiguration config = branchConfigRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("Branch configuration not found with id: " + configId));

        config.setPrice(configDTO.getPrice());
        config.setDurationMinutes(configDTO.getDurationMinutes());
        config.setCommissionRate(configDTO.getCommissionRate());
        config.setCommissionType(configDTO.getCommissionType());
        config.setFixedCommissionAmount(configDTO.getFixedCommissionAmount());
        config.setIsEnabled(configDTO.getIsEnabled());
        config.setRequiredSkills(configDTO.getRequiredSkills());
        config.setResourceRequirements(configDTO.getResourceRequirements());

        ServiceBranchConfiguration savedConfig = branchConfigRepository.save(config);
        return convertBranchConfigToDTO(savedConfig);
    }

    public List<ServiceBranchConfigurationDTO> getBranchConfigurations(Long serviceId) {
        return branchConfigRepository.findByServiceId(serviceId).stream()
                .map(this::convertBranchConfigToDTO)
                .collect(Collectors.toList());
    }

    public ServiceBranchConfigurationDTO getBranchConfiguration(Long serviceId, Long branchId) {
        ServiceBranchConfiguration config = branchConfigRepository.findByServiceIdAndBranchId(serviceId, branchId)
                .orElseThrow(() -> new RuntimeException("Branch configuration not found"));
        return convertBranchConfigToDTO(config);
    }

    // ========== SERVICE PACKAGE METHODS ==========

    public ServicePackageDTO createServicePackage(ServicePackageDTO packageDTO) {
        ServicePackage servicePackage = new ServicePackage();
        servicePackage.setName(packageDTO.getName());
        servicePackage.setDescription(packageDTO.getDescription());
        servicePackage.setPrice(packageDTO.getPrice());
        servicePackage.setDiscountPercentage(packageDTO.getDiscountPercentage());
        servicePackage.setIsActive(packageDTO.getIsActive());
        servicePackage.setCanSplitSessions(packageDTO.getCanSplitSessions());
        servicePackage.setMaxValidityDays(packageDTO.getMaxValidityDays());

        ServicePackage savedPackage = packageRepository.save(servicePackage);
        return convertPackageToDTO(savedPackage);
    }

    public ServicePackageDTO updateServicePackage(Long packageId, ServicePackageDTO packageDTO) {
        ServicePackage servicePackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Service package not found with id: " + packageId));

        servicePackage.setName(packageDTO.getName());
        servicePackage.setDescription(packageDTO.getDescription());
        servicePackage.setPrice(packageDTO.getPrice());
        servicePackage.setDiscountPercentage(packageDTO.getDiscountPercentage());
        servicePackage.setIsActive(packageDTO.getIsActive());
        servicePackage.setCanSplitSessions(packageDTO.getCanSplitSessions());
        servicePackage.setMaxValidityDays(packageDTO.getMaxValidityDays());

        ServicePackage savedPackage = packageRepository.save(servicePackage);
        return convertPackageToDTO(savedPackage);
    }

    public List<ServicePackageDTO> getAllServicePackages() {
        return packageRepository.findAll().stream()
                .map(this::convertPackageToDTO)
                .collect(Collectors.toList());
    }

    public List<ServicePackageDTO> getActiveServicePackages() {
        return packageRepository.findByIsActiveTrue().stream()
                .map(this::convertPackageToDTO)
                .collect(Collectors.toList());
    }

    public ServicePackageDTO getServicePackageById(Long packageId) {
        ServicePackage servicePackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Service package not found with id: " + packageId));
        return convertPackageToDTO(servicePackage);
    }

    // ========== SERVICE PROMOTION METHODS ==========

    public ServicePromotionDTO createPromotion(ServicePromotionDTO promotionDTO) {
        ServicePromotion promotion = new ServicePromotion();
        promotion.setName(promotionDTO.getName());
        promotion.setDescription(promotionDTO.getDescription());
        promotion.setDiscountPercentage(promotionDTO.getDiscountPercentage());
        promotion.setDiscountAmount(promotionDTO.getDiscountAmount());
        promotion.setStartDate(promotionDTO.getStartDate());
        promotion.setEndDate(promotionDTO.getEndDate());
        promotion.setIsActive(promotionDTO.getIsActive());
        promotion.setMaxUses(promotionDTO.getMaxUses());
        promotion.setMinServiceAmount(promotionDTO.getMinServiceAmount());
        promotion.setApplicableBranches(promotionDTO.getApplicableBranches());

        if (promotionDTO.getServiceId() != null) {
            Service service = serviceRepository.findById(promotionDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service not found with id: " + promotionDTO.getServiceId()));
            promotion.setService(service);
        }

        ServicePromotion savedPromotion = promotionRepository.save(promotion);
        return convertPromotionToDTO(savedPromotion);
    }

    public List<ServicePromotionDTO> getActivePromotions() {
        return promotionRepository.findCurrentlyActivePromotions(LocalDate.now()).stream()
                .map(this::convertPromotionToDTO)
                .collect(Collectors.toList());
    }

    public List<ServicePromotionDTO> getPromotionsForService(Long serviceId) {
        return promotionRepository.findActivePromotionsForService(serviceId, LocalDate.now()).stream()
                .map(this::convertPromotionToDTO)
                .collect(Collectors.toList());
    }

    // ========== SERVICE PRODUCT USAGE METHODS ==========

    public ServiceProductUsageDTO addProductUsage(Long serviceId, ServiceProductUsageDTO usageDTO) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + serviceId));

        ServiceProductUsage usage = new ServiceProductUsage();
        usage.setService(service);
        usage.setProductName(usageDTO.getProductName());
        usage.setProductSku(usageDTO.getProductSku());
        usage.setQuantity(usageDTO.getQuantity());
        usage.setUnitCost(usageDTO.getUnitCost());
        usage.setIsRequired(usageDTO.getIsRequired());
        usage.setNotes(usageDTO.getNotes());

        ServiceProductUsage savedUsage = productUsageRepository.save(usage);
        return convertProductUsageToDTO(savedUsage);
    }

    public List<ServiceProductUsageDTO> getProductUsagesForService(Long serviceId) {
        return productUsageRepository.findByServiceId(serviceId).stream()
                .map(this::convertProductUsageToDTO)
                .collect(Collectors.toList());
    }

    // ========== ANALYTICS METHODS ==========

    public List<ServiceAnalyticsDTO> getServiceAnalytics(Long serviceId, LocalDate startDate, LocalDate endDate) {
        return analyticsRepository.findByServiceIdAndAnalyticsDateBetween(serviceId, startDate, endDate).stream()
                .map(this::convertAnalyticsToDTO)
                .collect(Collectors.toList());
    }

    public List<ServiceAnalyticsDTO> getMostPopularServices(LocalDate startDate, LocalDate endDate) {
        return analyticsRepository.findMostPopularServices(startDate, endDate).stream()
                .map(this::convertAnalyticsToDTO)
                .collect(Collectors.toList());
    }

    public List<ServiceAnalyticsDTO> getMostProfitableServices(LocalDate startDate, LocalDate endDate) {
        return analyticsRepository.findMostProfitableServices(startDate, endDate).stream()
                .map(this::convertAnalyticsToDTO)
                .collect(Collectors.toList());
    }

    // ========== HELPER CONVERSION METHODS ==========

    private ServiceBranchConfigurationDTO convertBranchConfigToDTO(ServiceBranchConfiguration config) {
        ServiceBranchConfigurationDTO dto = new ServiceBranchConfigurationDTO();
        dto.setId(config.getId());
        dto.setServiceId(config.getService().getId());
        dto.setServiceName(config.getService().getName());
        dto.setBranchId(config.getBranch().getId());
        dto.setBranchName(config.getBranch().getName());
        dto.setPrice(config.getPrice());
        dto.setDurationMinutes(config.getDurationMinutes());
        dto.setCommissionRate(config.getCommissionRate());
        dto.setCommissionType(config.getCommissionType());
        dto.setFixedCommissionAmount(config.getFixedCommissionAmount());
        dto.setIsEnabled(config.getIsEnabled());
        dto.setRequiredSkills(config.getRequiredSkills());
        dto.setResourceRequirements(config.getResourceRequirements());
        
        if (config.getCreatedAt() != null) {
            dto.setCreatedAt(config.getCreatedAt().format(formatter));
        }
        
        if (config.getUpdatedAt() != null) {
            dto.setUpdatedAt(config.getUpdatedAt().format(formatter));
        }
        
        return dto;
    }

    private ServicePackageDTO convertPackageToDTO(ServicePackage servicePackage) {
        ServicePackageDTO dto = new ServicePackageDTO();
        dto.setId(servicePackage.getId());
        dto.setName(servicePackage.getName());
        dto.setDescription(servicePackage.getDescription());
        dto.setPrice(servicePackage.getPrice());
        dto.setDiscountPercentage(servicePackage.getDiscountPercentage());
        dto.setIsActive(servicePackage.getIsActive());
        dto.setTotalDurationMinutes(servicePackage.getTotalDurationMinutes());
        dto.setCanSplitSessions(servicePackage.getCanSplitSessions());
        dto.setMaxValidityDays(servicePackage.getMaxValidityDays());
        dto.setOriginalPrice(servicePackage.getOriginalPrice());
        dto.setDiscountAmount(servicePackage.getDiscountAmount());
        
        if (servicePackage.getCreatedAt() != null) {
            dto.setCreatedAt(servicePackage.getCreatedAt().format(formatter));
        }
        
        if (servicePackage.getUpdatedAt() != null) {
            dto.setUpdatedAt(servicePackage.getUpdatedAt().format(formatter));
        }
        
        return dto;
    }

    private ServicePromotionDTO convertPromotionToDTO(ServicePromotion promotion) {
        ServicePromotionDTO dto = new ServicePromotionDTO();
        dto.setId(promotion.getId());
        dto.setName(promotion.getName());
        dto.setDescription(promotion.getDescription());
        dto.setDiscountPercentage(promotion.getDiscountPercentage());
        dto.setDiscountAmount(promotion.getDiscountAmount());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        dto.setIsActive(promotion.getIsActive());
        dto.setMaxUses(promotion.getMaxUses());
        dto.setUsedCount(promotion.getUsedCount());
        dto.setMinServiceAmount(promotion.getMinServiceAmount());
        dto.setApplicableBranches(promotion.getApplicableBranches());
        dto.setIsCurrentlyActive(promotion.isCurrentlyActive());
        dto.setCanBeUsed(promotion.canBeUsed());
        
        if (promotion.getService() != null) {
            dto.setServiceId(promotion.getService().getId());
            dto.setServiceName(promotion.getService().getName());
        }
        
        if (promotion.getCreatedAt() != null) {
            dto.setCreatedAt(promotion.getCreatedAt().format(formatter));
        }
        
        if (promotion.getUpdatedAt() != null) {
            dto.setUpdatedAt(promotion.getUpdatedAt().format(formatter));
        }
        
        return dto;
    }

    private ServiceProductUsageDTO convertProductUsageToDTO(ServiceProductUsage usage) {
        ServiceProductUsageDTO dto = new ServiceProductUsageDTO();
        dto.setId(usage.getId());
        dto.setServiceId(usage.getService().getId());
        dto.setServiceName(usage.getService().getName());
        dto.setProductName(usage.getProductName());
        dto.setProductSku(usage.getProductSku());
        dto.setQuantity(usage.getQuantity());
        dto.setUnitCost(usage.getUnitCost());
        dto.setIsRequired(usage.getIsRequired());
        dto.setNotes(usage.getNotes());
        dto.setTotalCost(usage.getTotalCost());
        
        if (usage.getCreatedAt() != null) {
            dto.setCreatedAt(usage.getCreatedAt().format(formatter));
        }
        
        if (usage.getUpdatedAt() != null) {
            dto.setUpdatedAt(usage.getUpdatedAt().format(formatter));
        }
        
        return dto;
    }

    private ServiceAnalyticsDTO convertAnalyticsToDTO(ServiceAnalytics analytics) {
        ServiceAnalyticsDTO dto = new ServiceAnalyticsDTO();
        dto.setId(analytics.getId());
        dto.setServiceId(analytics.getService().getId());
        dto.setServiceName(analytics.getService().getName());
        dto.setAnalyticsDate(analytics.getAnalyticsDate());
        dto.setTotalBookings(analytics.getTotalBookings());
        dto.setTotalRevenue(analytics.getTotalRevenue());
        dto.setTotalCommission(analytics.getTotalCommission());
        dto.setTotalProductCost(analytics.getTotalProductCost());
        dto.setTotalProfit(analytics.getTotalProfit());
        dto.setAverageDurationMinutes(analytics.getAverageDurationMinutes());
        dto.setAverageRating(analytics.getAverageRating());
        dto.setCancellationCount(analytics.getCancellationCount());
        dto.setNoShowCount(analytics.getNoShowCount());
        dto.setProfitMargin(analytics.getProfitMargin());
        dto.setAverageRevenuePerBooking(analytics.getAverageRevenuePerBooking());
        
        if (analytics.getBranch() != null) {
            dto.setBranchId(analytics.getBranch().getId());
            dto.setBranchName(analytics.getBranch().getName());
        }
        
        if (analytics.getCreatedAt() != null) {
            dto.setCreatedAt(analytics.getCreatedAt().format(formatter));
        }
        
        if (analytics.getUpdatedAt() != null) {
            dto.setUpdatedAt(analytics.getUpdatedAt().format(formatter));
        }
        
        return dto;
    }
}


