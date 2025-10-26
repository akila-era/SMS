package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.AppointmentFeedbackDTO;
import com.hexalyte.salon.model.Appointment;
import com.hexalyte.salon.model.AppointmentFeedback;
import com.hexalyte.salon.repository.AppointmentFeedbackRepository;
import com.hexalyte.salon.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentFeedbackService {

    @Autowired
    private AppointmentFeedbackRepository feedbackRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationService notificationService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Submit feedback
    public AppointmentFeedbackDTO submitFeedback(AppointmentFeedbackDTO feedbackDTO) {
        // Validate appointment exists and is completed
        Appointment appointment = appointmentRepository.findById(feedbackDTO.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() != Appointment.Status.COMPLETED) {
            throw new RuntimeException("Feedback can only be submitted for completed appointments");
        }

        // Check if feedback already exists for this appointment
        Optional<AppointmentFeedback> existingFeedback = feedbackRepository.findByAppointmentId(feedbackDTO.getAppointmentId());
        if (existingFeedback.isPresent()) {
            throw new RuntimeException("Feedback already submitted for this appointment");
        }

        // Create feedback
        AppointmentFeedback feedback = new AppointmentFeedback();
        feedback.setAppointment(appointment);
        feedback.setOverallRating(feedbackDTO.getOverallRating());
        feedback.setServiceQualityRating(feedbackDTO.getServiceQualityRating());
        feedback.setStaffFriendlinessRating(feedbackDTO.getStaffFriendlinessRating());
        feedback.setCleanlinessRating(feedbackDTO.getCleanlinessRating());
        feedback.setValueForMoneyRating(feedbackDTO.getValueForMoneyRating());
        feedback.setComments(feedbackDTO.getComments());
        feedback.setWouldRecommend(feedbackDTO.getWouldRecommend());
        feedback.setIsAnonymous(feedbackDTO.getIsAnonymous());

        AppointmentFeedback savedFeedback = feedbackRepository.save(feedback);

        // Send thank you notification
        notificationService.sendFeedbackThankYouNotification(appointment, savedFeedback);

        return convertToDTO(savedFeedback);
    }

    // Get feedback by appointment
    public AppointmentFeedbackDTO getFeedbackByAppointment(Long appointmentId) {
        AppointmentFeedback feedback = feedbackRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        return convertToDTO(feedback);
    }

    // Get feedback by staff
    public List<AppointmentFeedbackDTO> getFeedbackByStaff(Long staffId) {
        return feedbackRepository.findByStaffId(staffId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get feedback by branch
    public List<AppointmentFeedbackDTO> getFeedbackByBranch(Long branchId) {
        return feedbackRepository.findByBranchId(branchId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get feedback by date range
    public List<AppointmentFeedbackDTO> getFeedbackByDateRange(LocalDate startDate, LocalDate endDate) {
        return feedbackRepository.findByDateRange(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get recent feedback
    public List<AppointmentFeedbackDTO> getRecentFeedback() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return feedbackRepository.findRecentFeedback(thirtyDaysAgo).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get feedback statistics
    public FeedbackStatsDTO getFeedbackStats(Long branchId) {
        FeedbackStatsDTO stats = new FeedbackStatsDTO();
        
        if (branchId != null) {
            stats.setAverageRating(feedbackRepository.getAverageRatingByBranch(branchId));
            stats.setTotalFeedback(feedbackRepository.countByBranchIdAndRating(branchId, 1) +
                                 feedbackRepository.countByBranchIdAndRating(branchId, 2) +
                                 feedbackRepository.countByBranchIdAndRating(branchId, 3) +
                                 feedbackRepository.countByBranchIdAndRating(branchId, 4) +
                                 feedbackRepository.countByBranchIdAndRating(branchId, 5));
            stats.setFiveStarCount(feedbackRepository.countByBranchIdAndRating(branchId, 5));
            stats.setFourStarCount(feedbackRepository.countByBranchIdAndRating(branchId, 4));
            stats.setThreeStarCount(feedbackRepository.countByBranchIdAndRating(branchId, 3));
            stats.setTwoStarCount(feedbackRepository.countByBranchIdAndRating(branchId, 2));
            stats.setOneStarCount(feedbackRepository.countByBranchIdAndRating(branchId, 1));
        } else {
            stats.setAverageRating(feedbackRepository.countByRating(1) + feedbackRepository.countByRating(2) + 
                                 feedbackRepository.countByRating(3) + feedbackRepository.countByRating(4) + 
                                 feedbackRepository.countByRating(5) > 0 ? 
                                 (feedbackRepository.countByRating(1) * 1.0 + feedbackRepository.countByRating(2) * 2.0 + 
                                  feedbackRepository.countByRating(3) * 3.0 + feedbackRepository.countByRating(4) * 4.0 + 
                                  feedbackRepository.countByRating(5) * 5.0) / 
                                 (feedbackRepository.countByRating(1) + feedbackRepository.countByRating(2) + 
                                  feedbackRepository.countByRating(3) + feedbackRepository.countByRating(4) + 
                                  feedbackRepository.countByRating(5)) : 0.0);
            stats.setTotalFeedback(feedbackRepository.countByRating(1) + feedbackRepository.countByRating(2) + 
                                 feedbackRepository.countByRating(3) + feedbackRepository.countByRating(4) + 
                                 feedbackRepository.countByRating(5));
            stats.setFiveStarCount(feedbackRepository.countByRating(5));
            stats.setFourStarCount(feedbackRepository.countByRating(4));
            stats.setThreeStarCount(feedbackRepository.countByRating(3));
            stats.setTwoStarCount(feedbackRepository.countByRating(2));
            stats.setOneStarCount(feedbackRepository.countByRating(1));
        }
        
        stats.setRecommendationRate(feedbackRepository.findByWouldRecommendTrue().size() * 100.0 / 
                                   Math.max(stats.getTotalFeedback(), 1));
        
        return stats;
    }

    // Update feedback
    public AppointmentFeedbackDTO updateFeedback(Long feedbackId, AppointmentFeedbackDTO feedbackDTO) {
        AppointmentFeedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        feedback.setOverallRating(feedbackDTO.getOverallRating());
        feedback.setServiceQualityRating(feedbackDTO.getServiceQualityRating());
        feedback.setStaffFriendlinessRating(feedbackDTO.getStaffFriendlinessRating());
        feedback.setCleanlinessRating(feedbackDTO.getCleanlinessRating());
        feedback.setValueForMoneyRating(feedbackDTO.getValueForMoneyRating());
        feedback.setComments(feedbackDTO.getComments());
        feedback.setWouldRecommend(feedbackDTO.getWouldRecommend());
        feedback.setIsAnonymous(feedbackDTO.getIsAnonymous());

        AppointmentFeedback savedFeedback = feedbackRepository.save(feedback);
        return convertToDTO(savedFeedback);
    }

    // Delete feedback
    public void deleteFeedback(Long feedbackId) {
        AppointmentFeedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        feedbackRepository.delete(feedback);
    }

    private AppointmentFeedbackDTO convertToDTO(AppointmentFeedback feedback) {
        AppointmentFeedbackDTO dto = new AppointmentFeedbackDTO();
        dto.setId(feedback.getId());
        dto.setAppointmentId(feedback.getAppointment().getId());
        dto.setAppointmentDetails(String.format("Appointment on %s with %s at %s", 
                feedback.getAppointment().getAppointmentDate(),
                feedback.getAppointment().getStaff().getFullName(),
                feedback.getAppointment().getBranch().getName()));
        dto.setOverallRating(feedback.getOverallRating());
        dto.setServiceQualityRating(feedback.getServiceQualityRating());
        dto.setStaffFriendlinessRating(feedback.getStaffFriendlinessRating());
        dto.setCleanlinessRating(feedback.getCleanlinessRating());
        dto.setValueForMoneyRating(feedback.getValueForMoneyRating());
        dto.setComments(feedback.getComments());
        dto.setWouldRecommend(feedback.getWouldRecommend());
        dto.setIsAnonymous(feedback.getIsAnonymous());

        if (feedback.getCreatedAt() != null) {
            dto.setCreatedAt(feedback.getCreatedAt().format(formatter));
        }

        return dto;
    }

    // Inner class for feedback statistics
    public static class FeedbackStatsDTO {
        private Double averageRating;
        private Long totalFeedback;
        private Long fiveStarCount;
        private Long fourStarCount;
        private Long threeStarCount;
        private Long twoStarCount;
        private Long oneStarCount;
        private Double recommendationRate;

        // Getters and Setters
        public Double getAverageRating() { return averageRating; }
        public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
        public Long getTotalFeedback() { return totalFeedback; }
        public void setTotalFeedback(Long totalFeedback) { this.totalFeedback = totalFeedback; }
        public Long getFiveStarCount() { return fiveStarCount; }
        public void setFiveStarCount(Long fiveStarCount) { this.fiveStarCount = fiveStarCount; }
        public Long getFourStarCount() { return fourStarCount; }
        public void setFourStarCount(Long fourStarCount) { this.fourStarCount = fourStarCount; }
        public Long getThreeStarCount() { return threeStarCount; }
        public void setThreeStarCount(Long threeStarCount) { this.threeStarCount = threeStarCount; }
        public Long getTwoStarCount() { return twoStarCount; }
        public void setTwoStarCount(Long twoStarCount) { this.twoStarCount = twoStarCount; }
        public Long getOneStarCount() { return oneStarCount; }
        public void setOneStarCount(Long oneStarCount) { this.oneStarCount = oneStarCount; }
        public Double getRecommendationRate() { return recommendationRate; }
        public void setRecommendationRate(Double recommendationRate) { this.recommendationRate = recommendationRate; }
    }
}
