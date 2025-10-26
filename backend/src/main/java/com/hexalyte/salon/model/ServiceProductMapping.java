package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_product_mapping")
public class ServiceProductMapping {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @NotNull(message = "Service ID is required")
    @Column(name = "service_id", nullable = false)
    private Long serviceId;
    
    @NotNull(message = "Product ID is required")
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @NotNull(message = "Default quantity is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Default quantity must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Default quantity must have at most 8 integer digits and 2 decimal places")
    @Column(name = "default_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal defaultQuantity;
    
    @Column(name = "is_required")
    private Boolean isRequired = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", insertable = false, updatable = false)
    private Service service;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Constructors
    public ServiceProductMapping() {}
    
    public ServiceProductMapping(Long serviceId, Long productId, BigDecimal defaultQuantity, Boolean isRequired) {
        this.serviceId = serviceId;
        this.productId = productId;
        this.defaultQuantity = defaultQuantity;
        this.isRequired = isRequired;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public BigDecimal getDefaultQuantity() {
        return defaultQuantity;
    }
    
    public void setDefaultQuantity(BigDecimal defaultQuantity) {
        this.defaultQuantity = defaultQuantity;
    }
    
    public Boolean getIsRequired() {
        return isRequired;
    }
    
    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Service getService() {
        return service;
    }
    
    public void setService(Service service) {
        this.service = service;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
}
