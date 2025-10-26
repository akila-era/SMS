package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.ServiceProductMappingDTO;
import com.hexalyte.salon.service.ServiceProductMappingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-product-mappings")
@CrossOrigin(origins = "*")
public class ServiceProductMappingController {
    
    @Autowired
    private ServiceProductMappingService serviceProductMappingService;
    
    @PostMapping
    public ResponseEntity<ServiceProductMappingDTO> createMapping(@Valid @RequestBody ServiceProductMappingDTO mappingDTO) {
        try {
            ServiceProductMappingDTO createdMapping = serviceProductMappingService.createMapping(mappingDTO);
            return new ResponseEntity<>(createdMapping, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{mappingId}")
    public ResponseEntity<ServiceProductMappingDTO> updateMapping(@PathVariable Long mappingId, 
                                                               @Valid @RequestBody ServiceProductMappingDTO mappingDTO) {
        try {
            ServiceProductMappingDTO updatedMapping = serviceProductMappingService.updateMapping(mappingId, mappingDTO);
            return new ResponseEntity<>(updatedMapping, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/{mappingId}")
    public ResponseEntity<ServiceProductMappingDTO> getMappingById(@PathVariable Long mappingId) {
        try {
            ServiceProductMappingDTO mapping = serviceProductMappingService.getMappingById(mappingId);
            return new ResponseEntity<>(mapping, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<ServiceProductMappingDTO>> getMappingsByService(@PathVariable Long serviceId) {
        List<ServiceProductMappingDTO> mappings = serviceProductMappingService.getMappingsByService(serviceId);
        return new ResponseEntity<>(mappings, HttpStatus.OK);
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ServiceProductMappingDTO>> getMappingsByProduct(@PathVariable Long productId) {
        List<ServiceProductMappingDTO> mappings = serviceProductMappingService.getMappingsByProduct(productId);
        return new ResponseEntity<>(mappings, HttpStatus.OK);
    }
    
    @GetMapping("/services")
    public ResponseEntity<List<ServiceProductMappingDTO>> getMappingsByServices(@RequestParam List<Long> serviceIds) {
        List<ServiceProductMappingDTO> mappings = serviceProductMappingService.getMappingsByServices(serviceIds);
        return new ResponseEntity<>(mappings, HttpStatus.OK);
    }
    
    @DeleteMapping("/{mappingId}")
    public ResponseEntity<Void> deleteMapping(@PathVariable Long mappingId) {
        try {
            serviceProductMappingService.deleteMapping(mappingId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/service/{serviceId}")
    public ResponseEntity<Void> deleteMappingsByService(@PathVariable Long serviceId) {
        serviceProductMappingService.deleteMappingsByService(serviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteMappingsByProduct(@PathVariable Long productId) {
        serviceProductMappingService.deleteMappingsByProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @DeleteMapping("/service/{serviceId}/product/{productId}")
    public ResponseEntity<Void> deleteMappingByServiceAndProduct(@PathVariable Long serviceId, 
                                                               @PathVariable Long productId) {
        serviceProductMappingService.deleteMappingByServiceAndProduct(serviceId, productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
