package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.SupplierDTO;
import com.hexalyte.salon.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
public class SupplierController {
    
    @Autowired
    private SupplierService supplierService;
    
    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        try {
            SupplierDTO createdSupplier = supplierService.createSupplier(supplierDTO);
            return new ResponseEntity<>(createdSupplier, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{supplierId}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Long supplierId, 
                                                    @Valid @RequestBody SupplierDTO supplierDTO) {
        try {
            SupplierDTO updatedSupplier = supplierService.updateSupplier(supplierId, supplierDTO);
            return new ResponseEntity<>(updatedSupplier, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable Long supplierId) {
        try {
            SupplierDTO supplier = supplierService.getSupplierById(supplierId);
            return new ResponseEntity<>(supplier, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping
    public ResponseEntity<Page<SupplierDTO>> getAllSuppliers(Pageable pageable) {
        Page<SupplierDTO> suppliers = supplierService.getAllSuppliers(pageable);
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<SupplierDTO>> getSuppliersByFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        Page<SupplierDTO> suppliers = supplierService.getSuppliersByFilters(name, status, pageable);
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<SupplierDTO>> getActiveSuppliers() {
        List<SupplierDTO> suppliers = supplierService.getActiveSuppliers();
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }
    
    @DeleteMapping("/{supplierId}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long supplierId) {
        try {
            supplierService.deleteSupplier(supplierId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{supplierId}/activate")
    public ResponseEntity<Void> activateSupplier(@PathVariable Long supplierId) {
        try {
            supplierService.activateSupplier(supplierId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
