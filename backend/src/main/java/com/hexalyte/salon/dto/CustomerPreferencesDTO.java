package com.hexalyte.salon.dto;

import java.util.List;

public class CustomerPreferencesDTO {
    
    private Long customerId;
    private String preferredStaffId;
    private String preferredStaffName;
    private String preferredBranchId;
    private String preferredBranchName;
    private String preferredTimeSlot; // e.g., "MORNING", "AFTERNOON", "EVENING"
    private List<String> preferredServices;
    private String communicationPreference; // "EMAIL", "SMS", "PHONE"
    private Boolean receivePromotions;
    private Boolean receiveReminders;
    private String notes;
    
    // Constructors
    public CustomerPreferencesDTO() {}
    
    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public String getPreferredStaffId() {
        return preferredStaffId;
    }
    
    public void setPreferredStaffId(String preferredStaffId) {
        this.preferredStaffId = preferredStaffId;
    }
    
    public String getPreferredStaffName() {
        return preferredStaffName;
    }
    
    public void setPreferredStaffName(String preferredStaffName) {
        this.preferredStaffName = preferredStaffName;
    }
    
    public String getPreferredBranchId() {
        return preferredBranchId;
    }
    
    public void setPreferredBranchId(String preferredBranchId) {
        this.preferredBranchId = preferredBranchId;
    }
    
    public String getPreferredBranchName() {
        return preferredBranchName;
    }
    
    public void setPreferredBranchName(String preferredBranchName) {
        this.preferredBranchName = preferredBranchName;
    }
    
    public String getPreferredTimeSlot() {
        return preferredTimeSlot;
    }
    
    public void setPreferredTimeSlot(String preferredTimeSlot) {
        this.preferredTimeSlot = preferredTimeSlot;
    }
    
    public List<String> getPreferredServices() {
        return preferredServices;
    }
    
    public void setPreferredServices(List<String> preferredServices) {
        this.preferredServices = preferredServices;
    }
    
    public String getCommunicationPreference() {
        return communicationPreference;
    }
    
    public void setCommunicationPreference(String communicationPreference) {
        this.communicationPreference = communicationPreference;
    }
    
    public Boolean getReceivePromotions() {
        return receivePromotions;
    }
    
    public void setReceivePromotions(Boolean receivePromotions) {
        this.receivePromotions = receivePromotions;
    }
    
    public Boolean getReceiveReminders() {
        return receiveReminders;
    }
    
    public void setReceiveReminders(Boolean receiveReminders) {
        this.receiveReminders = receiveReminders;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
