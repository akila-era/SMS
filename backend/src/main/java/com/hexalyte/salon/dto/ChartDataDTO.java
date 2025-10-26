package com.hexalyte.salon.dto;

import java.math.BigDecimal;

public class ChartDataDTO {
    
    private String label;
    private String value;
    private BigDecimal numericValue;
    private String color;
    private String category;

    // Constructors
    public ChartDataDTO() {}

    public ChartDataDTO(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public ChartDataDTO(String label, BigDecimal numericValue) {
        this.label = label;
        this.numericValue = numericValue;
    }

    public ChartDataDTO(String label, String value, BigDecimal numericValue) {
        this.label = label;
        this.value = value;
        this.numericValue = numericValue;
    }

    // Getters and Setters
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BigDecimal getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(BigDecimal numericValue) {
        this.numericValue = numericValue;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
