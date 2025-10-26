package com.hexalyte.salon.service;

import com.hexalyte.salon.dto.AttendanceDTO;
import com.hexalyte.salon.model.Attendance;
import com.hexalyte.salon.model.Staff;
import com.hexalyte.salon.repository.AttendanceRepository;
import com.hexalyte.salon.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StaffRepository staffRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AttendanceDTO checkIn(Long staffId, LocalDate workDate) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + staffId));

        // Check if already checked in today
        Optional<Attendance> existingAttendance = attendanceRepository.findByStaffIdAndWorkDate(staffId, workDate);
        if (existingAttendance.isPresent() && existingAttendance.get().getCheckIn() != null) {
            throw new RuntimeException("Staff already checked in for this date");
        }

        Attendance attendance;
        if (existingAttendance.isPresent()) {
            attendance = existingAttendance.get();
        } else {
            attendance = new Attendance(staff, workDate);
        }

        attendance.setCheckIn(LocalTime.now());
        attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
        
        // Check if late (assuming 9:00 AM is standard start time)
        LocalTime standardStartTime = LocalTime.of(9, 0);
        if (attendance.getCheckIn().isAfter(standardStartTime)) {
            attendance.setIsLate(true);
            attendance.setStatus(Attendance.AttendanceStatus.LATE);
        }

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToDTO(savedAttendance);
    }

    public AttendanceDTO checkOut(Long staffId, LocalDate workDate) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + staffId));

        Attendance attendance = attendanceRepository.findByStaffIdAndWorkDate(staffId, workDate)
                .orElseThrow(() -> new RuntimeException("No check-in record found for this date"));

        if (attendance.getCheckOut() != null) {
            throw new RuntimeException("Staff already checked out for this date");
        }

        attendance.setCheckOut(LocalTime.now());
        attendance.calculateHours();

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToDTO(savedAttendance);
    }

    public AttendanceDTO markAbsent(Long staffId, LocalDate workDate, String notes) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + staffId));

        Optional<Attendance> existingAttendance = attendanceRepository.findByStaffIdAndWorkDate(staffId, workDate);
        if (existingAttendance.isPresent()) {
            throw new RuntimeException("Attendance record already exists for this date");
        }

        Attendance attendance = new Attendance(staff, workDate);
        attendance.setStatus(Attendance.AttendanceStatus.ABSENT);
        attendance.setNotes(notes);

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToDTO(savedAttendance);
    }

    public AttendanceDTO markLeave(Long staffId, LocalDate workDate, String notes) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + staffId));

        Optional<Attendance> existingAttendance = attendanceRepository.findByStaffIdAndWorkDate(staffId, workDate);
        if (existingAttendance.isPresent()) {
            throw new RuntimeException("Attendance record already exists for this date");
        }

        Attendance attendance = new Attendance(staff, workDate);
        attendance.setStatus(Attendance.AttendanceStatus.LEAVE);
        attendance.setNotes(notes);

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToDTO(savedAttendance);
    }

    public AttendanceDTO markHalfDay(Long staffId, LocalDate workDate, String notes) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + staffId));

        Optional<Attendance> existingAttendance = attendanceRepository.findByStaffIdAndWorkDate(staffId, workDate);
        if (existingAttendance.isPresent()) {
            throw new RuntimeException("Attendance record already exists for this date");
        }

        Attendance attendance = new Attendance(staff, workDate);
        attendance.setStatus(Attendance.AttendanceStatus.HALF_DAY);
        attendance.setNotes(notes);

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToDTO(savedAttendance);
    }

    public List<AttendanceDTO> getAttendanceByStaff(Long staffId) {
        return attendanceRepository.findByStaffId(staffId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceDTO> getAttendanceByStaffAndDateRange(Long staffId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByStaffIdAndWorkDateBetween(staffId, startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceDTO> getAttendanceByDate(LocalDate workDate) {
        return attendanceRepository.findByWorkDate(workDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AttendanceDTO> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByWorkDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AttendanceDTO getAttendanceByStaffAndDate(Long staffId, LocalDate workDate) {
        return attendanceRepository.findByStaffIdAndWorkDate(staffId, workDate)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public AttendanceDTO updateAttendance(Long attendanceId, AttendanceDTO attendanceDTO) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance record not found with id: " + attendanceId));

        if (attendanceDTO.getCheckIn() != null) {
            attendance.setCheckIn(attendanceDTO.getCheckIn());
        }
        if (attendanceDTO.getCheckOut() != null) {
            attendance.setCheckOut(attendanceDTO.getCheckOut());
        }
        if (attendanceDTO.getStatus() != null) {
            attendance.setStatus(Attendance.AttendanceStatus.valueOf(attendanceDTO.getStatus()));
        }
        if (attendanceDTO.getNotes() != null) {
            attendance.setNotes(attendanceDTO.getNotes());
        }

        // Recalculate hours if check-in and check-out are both present
        if (attendance.getCheckIn() != null && attendance.getCheckOut() != null) {
            attendance.calculateHours();
        }

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToDTO(savedAttendance);
    }

    public void deleteAttendance(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance record not found with id: " + attendanceId));
        attendanceRepository.delete(attendance);
    }

    public List<AttendanceDTO> getActiveCheckIns() {
        LocalDate today = LocalDate.now();
        return attendanceRepository.findByWorkDate(today).stream()
                .filter(attendance -> attendance.getCheckIn() != null && attendance.getCheckOut() == null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AttendanceDTO getActiveCheckInByStaff(Long staffId) {
        LocalDate today = LocalDate.now();
        return attendanceRepository.findActiveCheckInByStaffAndDate(staffId, today)
                .map(this::convertToDTO)
                .orElse(null);
    }

    private AttendanceDTO convertToDTO(Attendance attendance) {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setId(attendance.getId());
        dto.setStaffId(attendance.getStaff().getId());
        dto.setStaffName(attendance.getStaff().getFullName());
        dto.setWorkDate(attendance.getWorkDate());
        dto.setCheckIn(attendance.getCheckIn());
        dto.setCheckOut(attendance.getCheckOut());
        dto.setTotalHours(attendance.getTotalHours());
        dto.setStatus(attendance.getStatus() != null ? attendance.getStatus().name() : null);
        dto.setNotes(attendance.getNotes());
        dto.setIsLate(attendance.getIsLate());
        dto.setOvertimeHours(attendance.getOvertimeHours());

        if (attendance.getCreatedAt() != null) {
            dto.setCreatedAt(attendance.getCreatedAt().format(formatter));
        }
        if (attendance.getUpdatedAt() != null) {
            dto.setUpdatedAt(attendance.getUpdatedAt().format(formatter));
        }

        return dto;
    }
}
