package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_usage")
public class InventoryUsage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @NotNull(message = "Appointment ID is required")
    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;
    
    @NotNull(message = "Service ID is required")
    @Column(name = "service_id", nullable = false)
    private Long serviceId;
    
    @NotNull(message = "Staff ID is required")
    @Column(name = "staff_id", nullable = false)
    private Long staffId;
    
    @NotNull(message = "Branch ID is required")
    @Column(name = "branch_id", nullable = false)
    private Long branchId;
    
    @NotNull(message = "Product ID is required")
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @NotNull(message = "Quantity used is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Quantity used must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Quantity used must have at most 8 integer digits and 2 decimal places")
    @Column(name = "quantity_used", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityUsed;
    
    @NotNull(message = "Unit cost is required")
    @DecimalMin(value = "0.0", message = "Unit cost must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Unit cost must have at most 8 integer digits and 2 decimal places")
    @Column(name = "unit_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitCost;
    
    @NotNull(message = "Total cost is required")
    @DecimalMin(value = "0.0", message = "Total cost must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Total cost must have at most 8 integer digits and 2 decimal places")
    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCost;
    
    @Column(name = "used_at")
    private LocalDateTime usedAt;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", insertable = false, updatable = false)
    private Appointment appointment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", insertable = false, updatable = false)
    private Service service;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", insertable = false, updatable = false)
    private Staff staff;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", insertable = false, updatable = false)
    private Branch branch;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
    
    @PrePersist
    protected void onCreate() {
        usedAt = LocalDateTime.now();
    }
    
    // Constructors
    public InventoryUsage() {}
    
    public InventoryUsage(Long appointmentId, Long serviceId, Long staffId, Long branchId, 
                         Long productId, BigDecimal quantityUsed, BigDecimal unitCost, BigDecimal totalCost) {
        this.appointmentId = appointmentId;
        this.serviceId = serviceId;
        this.staffId = staffId;
        this.branchId = branchId;
        this.productId = productId;
        this.quantityUsed = quantityUsed;
        this.unitCost = unitCost;
        this.totalCost = totalCost;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public Long getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
    
    public Long getStaffId() {
        return staffId;
    }
    
    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }
    
    public Long getBranchId() {
        return branchId;
    }
    
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public BigDecimal getQuantityUsed() {
        return quantityUsed;
    }
    
    public void setQuantityUsed(BigDecimal quantityUsed) {
        this.quantityUsed = quantityUsed;
    }
    
    public BigDecimal getUnitCost() {
        return unitCost;
    }
    
    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }
    
    public BigDecimal getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    
    public LocalDateTime getUsedAt() {
        return usedAt;
    }
    
    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }
    
    public Appointment getAppointment() {
        return appointment;
    }
    
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
    
    public Service getService() {
        return service;
    }
    
    public void setService(Service service) {
        this.service = service;
    }
    
    public Staff getStaff() {
        return staff;
    }
    
    public void setStaff(Staff staff) {
        this.staff = staff;
    }
    
    public Branch getBranch() {
        return branch;
    }
    
    public void setBranch(Branch branch) {
        this.branch = branch;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
}
