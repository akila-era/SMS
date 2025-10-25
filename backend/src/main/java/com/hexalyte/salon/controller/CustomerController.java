package com.hexalyte.salon.controller;

import com.hexalyte.salon.dto.CustomerDTO;
import com.hexalyte.salon.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam String q) {
        List<CustomerDTO> customers = customerService.searchCustomers(q);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/membership/{level}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<CustomerDTO>> getCustomersByMembershipLevel(@PathVariable String level) {
        List<CustomerDTO> customers = customerService.getCustomersByMembershipLevel(level);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/top")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<List<CustomerDTO>> getTopCustomers() {
        List<CustomerDTO> customers = customerService.getTopCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/phone/{phone}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<CustomerDTO> getCustomerByPhone(@PathVariable String phone) {
        CustomerDTO customer = customerService.getCustomerByPhone(phone);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/loyalty-points")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BRANCH_MANAGER') or hasRole('RECEPTIONIST')")
    public ResponseEntity<CustomerDTO> updateLoyaltyPoints(@PathVariable Long id, @RequestParam Integer points) {
        CustomerDTO updatedCustomer = customerService.updateLoyaltyPoints(id, points);
        return ResponseEntity.ok(updatedCustomer);
    }
}


