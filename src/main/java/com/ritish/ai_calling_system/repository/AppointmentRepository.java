package com.ritish.ai_calling_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ritish.ai_calling_system.entity.Appointment;
import java.util.List;
import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByStartTimeAndStatus(LocalDateTime startTime, String status);

    // for reschudule : cehck conflo t excluding current appointment id

    boolean existsByStartTimeAndStatusAndIdNot(LocalDateTime starTime, String status, Long id);

    List<Appointment> findByOrderByStartTimeAsc();
}
