package com.hexalyte.salon.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AppointmentFeedbackDTO {
    
    private Long id;
    
    @NotNull(message = "Appointment is required")
    private Long appointmentId;
    private String appointmentDetails;
    
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @NotNull(message = "Overall rating is required")
    private Integer overallRating;
    
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer serviceQualityRating;
    
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer staffFriendlinessRating;
    
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer cleanlinessRating;
    
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer valueForMoneyRating;
    
    private String comments;
    private Boolean wouldRecommend;
    private Boolean isAnonymous = false;
    private String createdAt;

    // Constructors
    public AppointmentFeedbackDTO() {}

    public AppointmentFeedbackDTO(Long appointmentId, Integer overallRating) {
        this.appointmentId = appointmentId;
        this.overallRating = overallRating;
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

    public String getAppointmentDetails() {
        return appointmentDetails;
    }

    public void setAppointmentDetails(String appointmentDetails) {
        this.appointmentDetails = appointmentDetails;
    }

    public Integer getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(Integer overallRating) {
        this.overallRating = overallRating;
    }

    public Integer getServiceQualityRating() {
        return serviceQualityRating;
    }

    public void setServiceQualityRating(Integer serviceQualityRating) {
        this.serviceQualityRating = serviceQualityRating;
    }

    public Integer getStaffFriendlinessRating() {
        return staffFriendlinessRating;
    }

    public void setStaffFriendlinessRating(Integer staffFriendlinessRating) {
        this.staffFriendlinessRating = staffFriendlinessRating;
    }

    public Integer getCleanlinessRating() {
        return cleanlinessRating;
    }

    public void setCleanlinessRating(Integer cleanlinessRating) {
        this.cleanlinessRating = cleanlinessRating;
    }

    public Integer getValueForMoneyRating() {
        return valueForMoneyRating;
    }

    public void setValueForMoneyRating(Integer valueForMoneyRating) {
        this.valueForMoneyRating = valueForMoneyRating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getWouldRecommend() {
        return wouldRecommend;
    }

    public void setWouldRecommend(Boolean wouldRecommend) {
        this.wouldRecommend = wouldRecommend;
    }

    public Boolean getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
