package com.task.WorkVista.service;

import com.task.WorkVista.dto.TimesheetCreateDTO;
import com.task.WorkVista.dto.TimesheetDTO;
import com.task.WorkVista.entity.Timesheet;
import com.task.WorkVista.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface TimesheetService {
    TimesheetDTO saveTimesheet(TimesheetCreateDTO timesheet);
    List<TimesheetDTO> getAllTimesheets();
    TimesheetDTO getTimesheetById(Long id);

    Timesheet updateTimesheet(Long id, Timesheet updatedTimesheet);
    List<TimesheetDTO> getTimesheetsByManager(Long managerId);

    List<TimesheetDTO> getTimesheetsByUser(Long user);

    List<Timesheet> getTimesheetsByDate(LocalDate date);

    List<Timesheet> getTimesheetsBetweenDates(LocalDate start, LocalDate end);

    void deleteTimesheet(Long id);
}