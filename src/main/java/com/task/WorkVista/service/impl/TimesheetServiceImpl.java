package com.task.WorkVista.service.impl;

import com.task.WorkVista.entity.Timesheet;
import com.task.WorkVista.entity.User;
import com.task.WorkVista.exception.ResourceNotFoundException;
import com.task.WorkVista.repository.TimesheetRepository;
import com.task.WorkVista.service.TimesheetService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TimesheetServiceImpl implements TimesheetService {

    private final TimesheetRepository timesheetRepository;

    public TimesheetServiceImpl(TimesheetRepository timesheetRepository) {
        this.timesheetRepository = timesheetRepository;
    }

    @Override
    public Timesheet saveTimesheet(Timesheet timesheet) {
        return timesheetRepository.save(timesheet);
    }

    @Override
    public List<Timesheet> getAllTimesheets() {
        return timesheetRepository.findAll();
    }

    @Override
    public Optional<Timesheet> getTimesheetById(Long id) {
        return Optional.ofNullable(timesheetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timesheet not found with id: " + id)));
    }
    @Override
    public Timesheet updateTimesheet(Long id, Timesheet updatedTimesheet) {
        Timesheet ts = timesheetRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Timesheet not found with id: "+id));
        ts.setTask(updatedTimesheet.getTask());
        ts.setUser(updatedTimesheet.getUser());
        ts.setDate(updatedTimesheet.getDate());
        ts.setHours(updatedTimesheet.getHours());
        ts.setDescription(updatedTimesheet.getDescription());
        ts.setStatus(updatedTimesheet.getStatus());
        return timesheetRepository.save(ts);
    }
    @Override
    public List<Timesheet> getTimesheetsByUser(User user) {
        return timesheetRepository.findByUser(user);
    }

    @Override
    public List<Timesheet> getTimesheetsByDate(LocalDate date) {
        return List.of();
    }
    @Override
    public List<Timesheet> getTimesheetsByManager(Long managerId) {
        return timesheetRepository.findTimesheetsByManager(managerId);
    }
    @Override
    public List<Timesheet> getTimesheetsBetweenDates(LocalDate start, LocalDate end) {
        return timesheetRepository.findByDateBetween(start, end);
    }

    @Override
    public void deleteTimesheet(Long id) {
        timesheetRepository.deleteById(id);
    }

}