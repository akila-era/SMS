package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.*;
import com.hexalyte.salon.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        List<ServiceDTO> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<ServiceDTO>> getActiveServices() {
        List<ServiceDTO> services = serviceService.getActiveServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = serviceService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<ServiceDTO>> getServicesByCategory(@PathVariable String category) {
        List<ServiceDTO> services = serviceService.getServicesByCategory(category);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<ServiceDTO>> searchServices(@RequestParam String name) {
        List<ServiceDTO> services = serviceService.searchServices(name);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<ServiceDTO> getServiceById(@PathVariable Long id) {
        ServiceDTO service = serviceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<ServiceDTO> createService(@Valid @RequestBody ServiceDTO serviceDTO) {
        ServiceDTO createdService = serviceService.createService(serviceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdService);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable Long id, @Valid @RequestBody ServiceDTO serviceDTO) {
        ServiceDTO updatedService = serviceService.updateService(id, serviceDTO);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<ServiceDTO> toggleServiceStatus(@PathVariable Long id) {
        ServiceDTO updatedService = serviceService.toggleServiceStatus(id);
        return ResponseEntity.ok(updatedService);
    }

    // ========== BRANCH-SPECIFIC CONFIGURATION ENDPOINTS ==========

    @PostMapping("/{serviceId}/branch-configurations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<ServiceBranchConfigurationDTO> createBranchConfiguration(
            @PathVariable Long serviceId, 
            @RequestParam Long branchId,
            @Valid @RequestBody ServiceBranchConfigurationDTO configDTO) {
        ServiceBranchConfigurationDTO createdConfig = serviceService.createBranchConfiguration(serviceId, branchId, configDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConfig);
    }

    @PutMapping("/branch-configurations/{configId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<ServiceBranchConfigurationDTO> updateBranchConfiguration(
            @PathVariable Long configId,
            @Valid @RequestBody ServiceBranchConfigurationDTO configDTO) {
        ServiceBranchConfigurationDTO updatedConfig = serviceService.updateBranchConfiguration(configId, configDTO);
        return ResponseEntity.ok(updatedConfig);
    }

    @GetMapping("/{serviceId}/branch-configurations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<ServiceBranchConfigurationDTO>> getBranchConfigurations(@PathVariable Long serviceId) {
        List<ServiceBranchConfigurationDTO> configs = serviceService.getBranchConfigurations(serviceId);
        return ResponseEntity.ok(configs);
    }

    @GetMapping("/{serviceId}/branch-configurations/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<ServiceBranchConfigurationDTO> getBranchConfiguration(
            @PathVariable Long serviceId, 
            @PathVariable Long branchId) {
        ServiceBranchConfigurationDTO config = serviceService.getBranchConfiguration(serviceId, branchId);
        return ResponseEntity.ok(config);
    }

    // ========== SERVICE PACKAGE ENDPOINTS ==========

    @PostMapping("/packages")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<ServicePackageDTO> createServicePackage(@Valid @RequestBody ServicePackageDTO packageDTO) {
        ServicePackageDTO createdPackage = serviceService.createServicePackage(packageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPackage);
    }

    @PutMapping("/packages/{packageId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<ServicePackageDTO> updateServicePackage(
            @PathVariable Long packageId,
            @Valid @RequestBody ServicePackageDTO packageDTO) {
        ServicePackageDTO updatedPackage = serviceService.updateServicePackage(packageId, packageDTO);
        return ResponseEntity.ok(updatedPackage);
    }

    @GetMapping("/packages")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<ServicePackageDTO>> getAllServicePackages() {
        List<ServicePackageDTO> packages = serviceService.getAllServicePackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/packages/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<ServicePackageDTO>> getActiveServicePackages() {
        List<ServicePackageDTO> packages = serviceService.getActiveServicePackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/packages/{packageId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<ServicePackageDTO> getServicePackageById(@PathVariable Long packageId) {
        ServicePackageDTO servicePackage = serviceService.getServicePackageById(packageId);
        return ResponseEntity.ok(servicePackage);
    }

    // ========== SERVICE PROMOTION ENDPOINTS ==========

    @PostMapping("/promotions")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<ServicePromotionDTO> createPromotion(@Valid @RequestBody ServicePromotionDTO promotionDTO) {
        ServicePromotionDTO createdPromotion = serviceService.createPromotion(promotionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPromotion);
    }

    @GetMapping("/promotions/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<ServicePromotionDTO>> getActivePromotions() {
        List<ServicePromotionDTO> promotions = serviceService.getActivePromotions();
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/{serviceId}/promotions")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<ServicePromotionDTO>> getPromotionsForService(@PathVariable Long serviceId) {
        List<ServicePromotionDTO> promotions = serviceService.getPromotionsForService(serviceId);
        return ResponseEntity.ok(promotions);
    }

    // ========== SERVICE PRODUCT USAGE ENDPOINTS ==========

    @PostMapping("/{serviceId}/product-usages")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<ServiceProductUsageDTO> addProductUsage(
            @PathVariable Long serviceId,
            @Valid @RequestBody ServiceProductUsageDTO usageDTO) {
        ServiceProductUsageDTO createdUsage = serviceService.addProductUsage(serviceId, usageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUsage);
    }

    @GetMapping("/{serviceId}/product-usages")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<ServiceProductUsageDTO>> getProductUsagesForService(@PathVariable Long serviceId) {
        List<ServiceProductUsageDTO> usages = serviceService.getProductUsagesForService(serviceId);
        return ResponseEntity.ok(usages);
    }

    // ========== ANALYTICS ENDPOINTS ==========

    @GetMapping("/{serviceId}/analytics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<ServiceAnalyticsDTO>> getServiceAnalytics(
            @PathVariable Long serviceId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<ServiceAnalyticsDTO> analytics = serviceService.getServiceAnalytics(serviceId, startDate, endDate);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/analytics/most-popular")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<ServiceAnalyticsDTO>> getMostPopularServices(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<ServiceAnalyticsDTO> analytics = serviceService.getMostPopularServices(startDate, endDate);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/analytics/most-profitable")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<ServiceAnalyticsDTO>> getMostProfitableServices(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<ServiceAnalyticsDTO> analytics = serviceService.getMostProfitableServices(startDate, endDate);
        return ResponseEntity.ok(analytics);
    }
}


