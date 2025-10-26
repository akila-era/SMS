package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.AppointmentDTO;
import com.hexalyte.salon.dto.AppointmentTemplateDTO;
import com.hexalyte.salon.service.AppointmentTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointment-templates")
@CrossOrigin(origins = "*")
public class AppointmentTemplateController {

    @Autowired
    private AppointmentTemplateService templateService;

    // Create template
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<AppointmentTemplateDTO> createTemplate(@Valid @RequestBody AppointmentTemplateDTO templateDTO) {
        AppointmentTemplateDTO createdTemplate = templateService.createTemplate(templateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTemplate);
    }

    // Get all templates
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentTemplateDTO>> getAllTemplates() {
        List<AppointmentTemplateDTO> templates = templateService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    // Get templates by branch
    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentTemplateDTO>> getTemplatesByBranch(@PathVariable Long branchId) {
        List<AppointmentTemplateDTO> templates = templateService.getTemplatesByBranch(branchId);
        return ResponseEntity.ok(templates);
    }

    // Get template by ID
    @GetMapping("/{templateId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<AppointmentTemplateDTO> getTemplateById(@PathVariable Long templateId) {
        AppointmentTemplateDTO template = templateService.getTemplateById(templateId);
        return ResponseEntity.ok(template);
    }

    // Search templates
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentTemplateDTO>> searchTemplates(
            @RequestParam String searchTerm,
            @RequestParam(required = false) Long branchId) {
        List<AppointmentTemplateDTO> templates = templateService.searchTemplates(searchTerm, branchId);
        return ResponseEntity.ok(templates);
    }

    // Create appointment from template
    @PostMapping("/{templateId}/create-appointment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> createAppointmentFromTemplate(
            @PathVariable Long templateId,
            @RequestParam Long customerId,
            @RequestParam Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) String startTime) {
        
        AppointmentDTO appointment = templateService.createAppointmentFromTemplate(
                templateId, customerId, staffId, appointmentDate, LocalTime.parse(startTime));
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    // Update template
    @PutMapping("/{templateId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<AppointmentTemplateDTO> updateTemplate(
            @PathVariable Long templateId, 
            @Valid @RequestBody AppointmentTemplateDTO templateDTO) {
        AppointmentTemplateDTO updatedTemplate = templateService.updateTemplate(templateId, templateDTO);
        return ResponseEntity.ok(updatedTemplate);
    }

    // Delete template
    @DeleteMapping("/{templateId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long templateId) {
        templateService.deleteTemplate(templateId);
        return ResponseEntity.noContent().build();
    }

    // Get most popular templates
    @GetMapping("/popular")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST') or hasRole('BEAUTICIAN')")
    public ResponseEntity<List<AppointmentTemplateDTO>> getMostPopularTemplates(
            @RequestParam(required = false) Long branchId) {
        List<AppointmentTemplateDTO> templates = templateService.getMostPopularTemplates(branchId);
        return ResponseEntity.ok(templates);
    }
}
