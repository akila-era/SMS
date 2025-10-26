package com.hexalyte.salon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    
    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must not exceed 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;
    
    @Size(max = 50, message = "Category must not exceed 50 characters")
    @Column(name = "category", length = 50)
    private String category;
    
    @Size(max = 50, message = "Brand must not exceed 50 characters")
    @Column(name = "brand", length = 50)
    private String brand;
    
    @Size(max = 20, message = "UOM must not exceed 20 characters")
    @Column(name = "uom", length = 20)
    private String uom;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    private ProductType productType;
    
    @NotNull(message = "Cost price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Cost price must have at most 8 integer digits and 2 decimal places")
    @Column(name = "cost_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal costPrice;
    
    @DecimalMin(value = "0.0", message = "Selling price must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Selling price must have at most 8 integer digits and 2 decimal places")
    @Column(name = "selling_price", precision = 10, scale = 2)
    private BigDecimal sellingPrice = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", message = "Alert quantity must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "Alert quantity must have at most 8 integer digits and 2 decimal places")
    @Column(name = "alert_quantity", precision = 10, scale = 2)
    private BigDecimal alertQuantity = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", message = "Tax rate must be greater than or equal to 0")
    @DecimalMax(value = "100.0", message = "Tax rate must not exceed 100%")
    @Digits(integer = 3, fraction = 2, message = "Tax rate must have at most 3 integer digits and 2 decimal places")
    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status = ProductStatus.ACTIVE;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Product() {}
    
    public Product(String name, String code, String category, String brand, String uom, 
                  ProductType productType, BigDecimal costPrice, BigDecimal sellingPrice, 
                  BigDecimal alertQuantity, BigDecimal taxRate) {
        this.name = name;
        this.code = code;
        this.category = category;
        this.brand = brand;
        this.uom = uom;
        this.productType = productType;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.alertQuantity = alertQuantity;
        this.taxRate = taxRate;
    }
    
    // Getters and Setters
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getUom() {
        return uom;
    }
    
    public void setUom(String uom) {
        this.uom = uom;
    }
    
    public ProductType getProductType() {
        return productType;
    }
    
    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
    
    public BigDecimal getCostPrice() {
        return costPrice;
    }
    
    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }
    
    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }
    
    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    
    public BigDecimal getAlertQuantity() {
        return alertQuantity;
    }
    
    public void setAlertQuantity(BigDecimal alertQuantity) {
        this.alertQuantity = alertQuantity;
    }
    
    public BigDecimal getTaxRate() {
        return taxRate;
    }
    
    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
    
    public ProductStatus getStatus() {
        return status;
    }
    
    public void setStatus(ProductStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Enums
    public enum ProductType {
        CONSUMABLE, RETAIL, TOOL
    }
    
    public enum ProductStatus {
        ACTIVE, INACTIVE
    }
}
