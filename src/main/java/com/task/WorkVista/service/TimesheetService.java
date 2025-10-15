package com.task.WorkVista.service;

import com.task.WorkVista.entity.Timesheet;
import com.task.WorkVista.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public interface TimesheetService {
    Timesheet saveTimesheet(Timesheet timesheet);
    List<Timesheet> getAllTimesheets();
    Optional<Timesheet> getTimesheetById(Long id);

    Timesheet updateTimesheet(Long id, Timesheet updatedTimesheet);
    List<Timesheet> getTimesheetsByManager(Long managerId);
    List<Timesheet> getTimesheetsByUser(User user);
    List<Timesheet> getTimesheetsByDate(LocalDate date);

    List<Timesheet> getTimesheetsBetweenDates(LocalDate start, LocalDate end);

    void deleteTimesheet(Long id);
}