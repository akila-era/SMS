package com.hexalyte.salon.dto;

import java.math.BigDecimal;
import java.util.List;

public class InventoryDashboardDTO {
    
    private Long totalProducts;
    private Long lowStockCount;
    private Long outOfStockCount;
    private BigDecimal totalStockValue;
    private List<LowStockAlertDTO> lowStockAlerts;
    private List<InventoryDTO> recentTransactions;
    private List<ProductUsageSummaryDTO> topUsedProducts;
    private List<BranchStockSummaryDTO> branchStockSummary;
    
    // Constructors
    public InventoryDashboardDTO() {}
    
    public InventoryDashboardDTO(Long totalProducts, Long lowStockCount, Long outOfStockCount, 
                               BigDecimal totalStockValue) {
        this.totalProducts = totalProducts;
        this.lowStockCount = lowStockCount;
        this.outOfStockCount = outOfStockCount;
        this.totalStockValue = totalStockValue;
    }
    
    // Getters and Setters
    public Long getTotalProducts() {
        return totalProducts;
    }
    
    public void setTotalProducts(Long totalProducts) {
        this.totalProducts = totalProducts;
    }
    
    public Long getLowStockCount() {
        return lowStockCount;
    }
    
    public void setLowStockCount(Long lowStockCount) {
        this.lowStockCount = lowStockCount;
    }
    
    public Long getOutOfStockCount() {
        return outOfStockCount;
    }
    
    public void setOutOfStockCount(Long outOfStockCount) {
        this.outOfStockCount = outOfStockCount;
    }
    
    public BigDecimal getTotalStockValue() {
        return totalStockValue;
    }
    
    public void setTotalStockValue(BigDecimal totalStockValue) {
        this.totalStockValue = totalStockValue;
    }
    
    public List<LowStockAlertDTO> getLowStockAlerts() {
        return lowStockAlerts;
    }
    
    public void setLowStockAlerts(List<LowStockAlertDTO> lowStockAlerts) {
        this.lowStockAlerts = lowStockAlerts;
    }
    
    public List<InventoryDTO> getRecentTransactions() {
        return recentTransactions;
    }
    
    public void setRecentTransactions(List<InventoryDTO> recentTransactions) {
        this.recentTransactions = recentTransactions;
    }
    
    public List<ProductUsageSummaryDTO> getTopUsedProducts() {
        return topUsedProducts;
    }
    
    public void setTopUsedProducts(List<ProductUsageSummaryDTO> topUsedProducts) {
        this.topUsedProducts = topUsedProducts;
    }
    
    public List<BranchStockSummaryDTO> getBranchStockSummary() {
        return branchStockSummary;
    }
    
    public void setBranchStockSummary(List<BranchStockSummaryDTO> branchStockSummary) {
        this.branchStockSummary = branchStockSummary;
    }
    
    // Inner classes for summary data
    public static class ProductUsageSummaryDTO {
        private Long productId;
        private String productName;
        private String productCode;
        private BigDecimal totalQuantityUsed;
        private BigDecimal totalCost;
        private Long usageCount;
        
        // Constructors
        public ProductUsageSummaryDTO() {}
        
        public ProductUsageSummaryDTO(Long productId, String productName, String productCode, 
                                    BigDecimal totalQuantityUsed, BigDecimal totalCost, Long usageCount) {
            this.productId = productId;
            this.productName = productName;
            this.productCode = productCode;
            this.totalQuantityUsed = totalQuantityUsed;
            this.totalCost = totalCost;
            this.usageCount = usageCount;
        }
        
        // Getters and Setters
        public Long getProductId() {
            return productId;
        }
        
        public void setProductId(Long productId) {
            this.productId = productId;
        }
        
        public String getProductName() {
            return productName;
        }
        
        public void setProductName(String productName) {
            this.productName = productName;
        }
        
        public String getProductCode() {
            return productCode;
        }
        
        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }
        
        public BigDecimal getTotalQuantityUsed() {
            return totalQuantityUsed;
        }
        
        public void setTotalQuantityUsed(BigDecimal totalQuantityUsed) {
            this.totalQuantityUsed = totalQuantityUsed;
        }
        
        public BigDecimal getTotalCost() {
            return totalCost;
        }
        
        public void setTotalCost(BigDecimal totalCost) {
            this.totalCost = totalCost;
        }
        
        public Long getUsageCount() {
            return usageCount;
        }
        
        public void setUsageCount(Long usageCount) {
            this.usageCount = usageCount;
        }
    }
    
    public static class BranchStockSummaryDTO {
        private Long branchId;
        private String branchName;
        private String branchCode;
        private Long totalProducts;
        private Long lowStockProducts;
        private Long outOfStockProducts;
        private BigDecimal totalValue;
        
        // Constructors
        public BranchStockSummaryDTO() {}
        
        public BranchStockSummaryDTO(Long branchId, String branchName, String branchCode, 
                                   Long totalProducts, Long lowStockProducts, Long outOfStockProducts, 
                                   BigDecimal totalValue) {
            this.branchId = branchId;
            this.branchName = branchName;
            this.branchCode = branchCode;
            this.totalProducts = totalProducts;
            this.lowStockProducts = lowStockProducts;
            this.outOfStockProducts = outOfStockProducts;
            this.totalValue = totalValue;
        }
        
        // Getters and Setters
        public Long getBranchId() {
            return branchId;
        }
        
        public void setBranchId(Long branchId) {
            this.branchId = branchId;
        }
        
        public String getBranchName() {
            return branchName;
        }
        
        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }
        
        public String getBranchCode() {
            return branchCode;
        }
        
        public void setBranchCode(String branchCode) {
            this.branchCode = branchCode;
        }
        
        public Long getTotalProducts() {
            return totalProducts;
        }
        
        public void setTotalProducts(Long totalProducts) {
            this.totalProducts = totalProducts;
        }
        
        public Long getLowStockProducts() {
            return lowStockProducts;
        }
        
        public void setLowStockProducts(Long lowStockProducts) {
            this.lowStockProducts = lowStockProducts;
        }
        
        public Long getOutOfStockProducts() {
            return outOfStockProducts;
        }
        
        public void setOutOfStockProducts(Long outOfStockProducts) {
            this.outOfStockProducts = outOfStockProducts;
        }
        
        public BigDecimal getTotalValue() {
            return totalValue;
        }
        
        public void setTotalValue(BigDecimal totalValue) {
            this.totalValue = totalValue;
        }
    }
}
