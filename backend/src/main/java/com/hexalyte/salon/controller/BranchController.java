package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.*;
import com.hexalyte.salon.model.Branch;
import com.hexalyte.salon.security.AuditLogger;
import com.hexalyte.salon.security.BranchAccessControlService;
import com.hexalyte.salon.security.RequireBranchAccess;
import com.hexalyte.salon.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/branches")
@CrossOrigin(origins = "*")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @Autowired
    private BranchAccessControlService branchAccessControlService;

    @Autowired
    private AuditLogger auditLogger;

    // Basic CRUD Operations
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<BranchDTO>> getAllBranches() {
        List<BranchDTO> branches = branchService.getAllBranches();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<BranchDTO>> getActiveBranches() {
        List<BranchDTO> branches = branchService.getActiveBranches();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BranchDTO>> getBranchesByStatus(@PathVariable Branch.BranchStatus status) {
        List<BranchDTO> branches = branchService.getBranchesByStatus(status);
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    @RequireBranchAccess(branchIdParam = "id", level = RequireBranchAccess.BranchAccessLevel.VIEW)
    public ResponseEntity<BranchDTO> getBranchById(@PathVariable Long id) {
        BranchDTO branch = branchService.getBranchById(id);
        return ResponseEntity.ok(branch);
    }

    @GetMapping("/code/{branchCode}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<BranchDTO> getBranchByCode(@PathVariable String branchCode) {
        BranchDTO branch = branchService.getBranchByCode(branchCode);
        return ResponseEntity.ok(branch);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchDTO> createBranch(@Valid @RequestBody BranchDTO branchDTO, Authentication authentication, HttpServletRequest request) {
        Long currentUserId = getCurrentUserId(authentication);
        BranchDTO createdBranch = branchService.createBranch(branchDTO, currentUserId);
        
        // Audit logging
        String username = authentication.getName();
        String ipAddress = auditLogger.getClientIpAddress(request);
        auditLogger.logBranchCreated(username, createdBranch.getBranchName(), createdBranch.getBranchCode(), ipAddress);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBranch);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchDTO> updateBranch(@PathVariable Long id, @Valid @RequestBody BranchDTO branchDTO, Authentication authentication, HttpServletRequest request) {
        Long currentUserId = getCurrentUserId(authentication);
        BranchDTO updatedBranch = branchService.updateBranch(id, branchDTO, currentUserId);
        
        // Audit logging
        String username = authentication.getName();
        String ipAddress = auditLogger.getClientIpAddress(request);
        auditLogger.logBranchUpdated(username, updatedBranch.getBranchName(), updatedBranch.getBranchCode(), "Branch details updated", ipAddress);
        
        return ResponseEntity.ok(updatedBranch);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id, Authentication authentication, HttpServletRequest request) {
        // Get branch info before deletion for audit logging
        BranchDTO branch = branchService.getBranchById(id);
        
        branchService.deleteBranch(id);
        
        // Audit logging
        String username = authentication.getName();
        String ipAddress = auditLogger.getClientIpAddress(request);
        auditLogger.logBranchDeleted(username, branch.getBranchName(), branch.getBranchCode(), ipAddress);
        
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchDTO> toggleBranchStatus(@PathVariable Long id, Authentication authentication, HttpServletRequest request) {
        // Get branch info before status change for audit logging
        BranchDTO originalBranch = branchService.getBranchById(id);
        String oldStatus = originalBranch.getStatus().name();
        
        Long currentUserId = getCurrentUserId(authentication);
        BranchDTO updatedBranch = branchService.toggleBranchStatus(id, currentUserId);
        
        // Audit logging
        String username = authentication.getName();
        String ipAddress = auditLogger.getClientIpAddress(request);
        auditLogger.logBranchStatusChanged(username, updatedBranch.getBranchName(), updatedBranch.getBranchCode(), 
            oldStatus, updatedBranch.getStatus().name(), ipAddress);
        
        return ResponseEntity.ok(updatedBranch);
    }

    // Search and Filter Operations
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<BranchDTO>> searchBranches(@RequestParam String q) {
        List<BranchDTO> branches = branchService.searchBranches(q);
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/city/{city}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<BranchDTO>> getBranchesByCity(@PathVariable String city) {
        List<BranchDTO> branches = branchService.getBranchesByCity(city);
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/manager/{managerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BranchDTO>> getBranchesByManager(@PathVariable Long managerId) {
        List<BranchDTO> branches = branchService.getBranchesByManager(managerId);
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/without-manager")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BranchDTO>> getBranchesWithoutManager() {
        List<BranchDTO> branches = branchService.getBranchesWithoutManager();
        return ResponseEntity.ok(branches);
    }

    // Branch User Access Management
    @PostMapping("/{branchId}/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchUserAccessDTO> assignUserToBranch(
            @PathVariable Long branchId, 
            @Valid @RequestBody BranchUserAccessDTO accessDTO) {
        accessDTO.setBranchId(branchId);
        BranchUserAccessDTO createdAccess = branchService.assignUserToBranch(accessDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccess);
    }

    @GetMapping("/{branchId}/users")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    @RequireBranchAccess(branchIdParam = "branchId", level = RequireBranchAccess.BranchAccessLevel.STAFF_MANAGEMENT)
    public ResponseEntity<List<BranchUserAccessDTO>> getBranchUserAccess(@PathVariable Long branchId) {
        List<BranchUserAccessDTO> accessList = branchService.getBranchUserAccess(branchId);
        return ResponseEntity.ok(accessList);
    }

    @GetMapping("/users/{userId}/branches")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<List<BranchUserAccessDTO>> getUserBranchAccess(@PathVariable Long userId) {
        List<BranchUserAccessDTO> accessList = branchService.getUserBranchAccess(userId);
        return ResponseEntity.ok(accessList);
    }

    @DeleteMapping("/{branchId}/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeUserFromBranch(@PathVariable Long branchId, @PathVariable Long userId) {
        branchService.removeUserFromBranch(userId, branchId);
        return ResponseEntity.noContent().build();
    }

    // Dashboard and Analytics
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchDashboardDTO> getBranchDashboard() {
        BranchDashboardDTO dashboard = branchService.getBranchDashboard();
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/performance/top")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BranchPerformanceDTO>> getTopPerformingBranches(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "5") int limit) {
        List<BranchPerformanceDTO> branches = branchService.getTopPerformingBranches(startDate, endDate, limit);
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/performance/comparison")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BranchPerformanceDTO>> getBranchPerformanceComparison(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<BranchPerformanceDTO> comparison = branchService.getBranchPerformanceComparison(startDate, endDate);
        return ResponseEntity.ok(comparison);
    }

    @GetMapping("/performance/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BranchPerformanceDTO>> getInactiveBranches(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<BranchPerformanceDTO> inactiveBranches = branchService.getInactiveBranches(startDate, endDate);
        return ResponseEntity.ok(inactiveBranches);
    }

    // Helper method to get current user ID
    private Long getCurrentUserId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            // This is a simplified approach - in a real application, you might want to get the user ID from the JWT token
            // or from a custom UserDetails implementation
            return 1L; // Placeholder - replace with actual user ID extraction logic
        }
        throw new RuntimeException("Unable to determine current user");
    }
}


