package com.task.WorkVista.repository;

import com.task.WorkVista.dto.TimesheetDTO;
import com.task.WorkVista.entity.Timesheet;
import com.task.WorkVista.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByUser(User user);
    List<Timesheet> findByDate(LocalDate date);
    @Query("SELECT t FROM Timesheet t WHERE t.task.manager.id = :managerId")
    List<Timesheet> findTimesheetsByManager(@Param("managerId") Long managerId);

    List<Timesheet> findByDateBetween(LocalDate start, LocalDate end);
    @Query("SELECT t FROM Timesheet t WHERE t.user.id = :userId")
    List<Timesheet> findByUserId(@Param("userId") Long userId);
}