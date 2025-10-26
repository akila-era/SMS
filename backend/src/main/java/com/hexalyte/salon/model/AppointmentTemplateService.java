package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "appointment_template_services")
public class AppointmentTemplateService {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private AppointmentTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "Commission rate must be non-negative")
    @Column(name = "commission_rate")
    private BigDecimal commissionRate;

    @Column(name = "service_order")
    private Integer serviceOrder = 0; // Order of services in the template

    // Constructors
    public AppointmentTemplateService() {}

    public AppointmentTemplateService(AppointmentTemplate template, Service service, BigDecimal price, BigDecimal commissionRate) {
        this.template = template;
        this.service = service;
        this.price = price;
        this.commissionRate = commissionRate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppointmentTemplate getTemplate() {
        return template;
    }

    public void setTemplate(AppointmentTemplate template) {
        this.template = template;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(BigDecimal commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Integer getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(Integer serviceOrder) {
        this.serviceOrder = serviceOrder;
    }
}
