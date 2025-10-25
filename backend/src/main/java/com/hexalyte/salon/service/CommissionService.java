package com.hexalyte.salon.service;

import com.hexalyte.salon.model.Appointment;
import com.hexalyte.salon.model.AppointmentService;
import com.hexalyte.salon.model.Commission;
import com.hexalyte.salon.repository.CommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
public class CommissionService {

    @Autowired
    private CommissionRepository commissionRepository;

    public void calculateCommissions(Appointment appointment) {
        for (AppointmentService appointmentService : appointment.getAppointmentServices()) {
            // Calculate commission: Service Price × Commission Rate / 100
            BigDecimal commissionAmount = appointmentService.getPrice()
                    .multiply(appointmentService.getCommissionRate())
                    .divide(new BigDecimal("100"));

            Commission commission = new Commission();
            commission.setStaff(appointment.getStaff());
            commission.setAppointment(appointment);
            commission.setService(appointmentService.getService());
            commission.setBranch(appointment.getBranch());
            commission.setAmount(commissionAmount);
            commission.setCommissionRate(appointmentService.getCommissionRate());
            commission.setCommissionDate(LocalDate.now());
            commission.setStatus(Commission.Status.PENDING);

            commissionRepository.save(commission);
        }
    }
}


