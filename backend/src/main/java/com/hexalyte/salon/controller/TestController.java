package com.hexalyte.salon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Salon Management System");
        response.put("version", "1.0.0");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "Multi-Branch Salon Management System");
        response.put("description", "A comprehensive salon management system with staff commission tracking");
        response.put("features", new String[]{
            "Multi-branch management",
            "Staff commission tracking", 
            "Appointment scheduling",
            "Customer management",
            "Inventory control",
            "Billing & payments",
            "Reports & analytics"
        });
        return response;
    }
}
