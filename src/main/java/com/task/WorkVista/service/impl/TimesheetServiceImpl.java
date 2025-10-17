package com.task.WorkVista.service.impl;

import com.task.WorkVista.dto.TimesheetCreateDTO;
import com.task.WorkVista.dto.TimesheetDTO;
import com.task.WorkVista.dto.TimesheetWrapper;
import com.task.WorkVista.entity.*;
import com.task.WorkVista.exception.ResourceNotFoundException;
import com.task.WorkVista.repository.TaskRepository;
import com.task.WorkVista.repository.TimesheetRepository;
import com.task.WorkVista.repository.UserRepository;
import com.task.WorkVista.service.TimesheetService;
import com.task.WorkVista.util.EntityReferenceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimesheetServiceImpl implements TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final UserRepository userRepository;
    private  final TaskRepository taskRepository;
    @Autowired
    EntityReferenceResolver resolver;

    public TimesheetServiceImpl(TimesheetRepository timesheetRepository,UserRepository userRepository, TaskRepository taskRepository) {
        this.timesheetRepository = timesheetRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public TimesheetDTO saveTimesheet(TimesheetCreateDTO dto) {
        Timesheet timesheet = new Timesheet();
        timesheet.setDate(dto.getDate());
        timesheet.setDescription(dto.getDescription());
        timesheet.setHours(dto.getHours());

        if (dto.getManager() != null) {
            User manager = userRepository.findById(dto.getManager())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + dto.getManager()));
            timesheet.setManager(manager);
        }if (dto.getUser() != null) {
            User user = userRepository.findById(dto.getUser())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getManager()));
            timesheet.setUser(user);
        }
        if (dto.getTask() != null) {
            Task task = taskRepository.findById(dto.getTask())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + dto.getManager()));
            timesheet.setTask(task);
        }

        Timesheet saved = timesheetRepository.save(timesheet);

        // Force initialization of lazy-loaded fields for DTO conversion
        if (saved.getTask() != null) {
            saved.getTask().getId();
            saved.getTask().getName();
        }
        if (saved.getUser() != null) {
            saved.getUser().getId();
            saved.getUser().getName();
            saved.getUser().getEmail();
        }
        if (saved.getManager() != null) {
            saved.getManager().getId();
            saved.getManager().getName();
            saved.getManager().getEmail();
        }

        return TimesheetWrapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimesheetDTO> getAllTimesheets() {
        return timesheetRepository.findAll()
                .stream()
                .map(ts -> {
                    // Force initialization
                    if (ts.getTask() != null) ts.getTask().getName();
                    if (ts.getUser() != null) ts.getUser().getName();
                    if (ts.getManager() != null) ts.getManager().getName();
                    return TimesheetWrapper.toDTO(ts);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TimesheetDTO getTimesheetById(Long id) {
        Timesheet ts = timesheetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timesheet not found with id: " + id));

        // Force initialization
        if (ts.getTask() != null) ts.getTask().getName();
        if (ts.getUser() != null) ts.getUser().getName();
        if (ts.getManager() != null) ts.getManager().getName();

        return TimesheetWrapper.toDTO(ts);
    }

    @Override
    @Transactional
    public Timesheet updateTimesheet(Long id, Timesheet updatedTimesheet) {
        Timesheet ts = timesheetRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Timesheet not found with id: "+id));

        // Use resolver to ensure proper entity references
        resolver.resolve(updatedTimesheet);

        ts.setTask(updatedTimesheet.getTask());
        ts.setUser(updatedTimesheet.getUser());
        ts.setManager(updatedTimesheet.getManager());
        ts.setDate(updatedTimesheet.getDate());
        ts.setHours(updatedTimesheet.getHours());
        ts.setDescription(updatedTimesheet.getDescription());
        ts.setStatus(updatedTimesheet.getStatus());
        ts.setLocation(updatedTimesheet.getLocation());

        Timesheet saved = timesheetRepository.save(ts);

        // Force initialization
        if (saved.getTask() != null) {
            saved.getTask().getId();
            saved.getTask().getName();
        }
        if (saved.getUser() != null) {
            saved.getUser().getId();
            saved.getUser().getName();
            saved.getUser().getEmail();
        }
        if (saved.getManager() != null) {
            saved.getManager().getId();
            saved.getManager().getName();
            saved.getManager().getEmail();
        }

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimesheetDTO> getTimesheetsByUser(Long userId) {
        List<Timesheet> timesheets = timesheetRepository.findByUserId(userId);
        return timesheets.stream()
                .map(ts -> {
                    if (ts.getTask() != null) ts.getTask().getName();
                    if (ts.getUser() != null) ts.getUser().getName();
                    if (ts.getManager() != null) ts.getManager().getName();
                    return TimesheetWrapper.toDTO(ts);
                })
                .toList();
    }

    @Override
    public List<Timesheet> getTimesheetsByDate(LocalDate date) {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimesheetDTO> getTimesheetsByManager(Long managerId) {
        List<Timesheet> timesheets = timesheetRepository.findTimesheetsByManager(managerId);
        return timesheets.stream()
                .map(ts -> {
                    if (ts.getTask() != null) ts.getTask().getName();
                    if (ts.getUser() != null) ts.getUser().getName();
                    if (ts.getManager() != null) ts.getManager().getName();
                    return TimesheetWrapper.toDTO(ts);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Timesheet> getTimesheetsBetweenDates(LocalDate start, LocalDate end) {
        List<Timesheet> timesheets = timesheetRepository.findByDateBetween(start, end);
        // Force initialization
        timesheets.forEach(ts -> {
            if (ts.getTask() != null) ts.getTask().getName();
            if (ts.getUser() != null) ts.getUser().getName();
            if (ts.getManager() != null) ts.getManager().getName();
        });
        return timesheets;
    }

    @Override
    public void deleteTimesheet(Long id) {
        timesheetRepository.deleteById(id);
    }
}