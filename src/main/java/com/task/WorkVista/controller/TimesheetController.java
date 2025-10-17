package com.task.WorkVista.controller;

import com.task.WorkVista.dto.TimesheetCreateDTO;
import com.task.WorkVista.dto.TimesheetDTO;
import com.task.WorkVista.dto.TimesheetWrapper;
import com.task.WorkVista.entity.Timesheet;
import com.task.WorkVista.repository.TaskRepository;
import com.task.WorkVista.service.TaskService;
import com.task.WorkVista.service.TimesheetService;
import com.task.WorkVista.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timesheets")
public class TimesheetController {

    private final TimesheetService timesheetService;

    public TimesheetController(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    TaskRepository taskRepository;

    @PostMapping
    public TimesheetDTO createTimesheet(@RequestBody TimesheetCreateDTO timesheet) {
        System.out.println(timesheet);
        return timesheetService.saveTimesheet(timesheet);
    }

    @GetMapping
    public List<TimesheetDTO> getAllTimesheets() {
        return timesheetService.getAllTimesheets();
    }

    @GetMapping("/{id}")
    public TimesheetDTO getTimesheetById(@PathVariable Long id) {
        return timesheetService.getTimesheetById(id);
    }

    @PutMapping("/{id}")
    public TimesheetDTO updateTimesheet(@PathVariable Long id, @RequestBody Timesheet updatedTimesheet) {
        return TimesheetWrapper.toDTO(timesheetService.updateTimesheet(id, updatedTimesheet));
    }

    @GetMapping("/user/{userId}")
    public List<TimesheetDTO> getTimesheetsByUser(@PathVariable Long userId) {
        return timesheetService.getTimesheetsByUser(userId);
    }

    @GetMapping("/manager/{userId}")
    public List<TimesheetDTO> getTimesheetsByManager(@PathVariable Long userId) {
        return timesheetService.getTimesheetsByManager(userId);
    }

    @GetMapping("/range")
    public List<TimesheetDTO> getTimesheetsBetweenDates(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        return timesheetService.getTimesheetsBetweenDates(start, end).stream()
                .map(TimesheetWrapper::toDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteTimesheet(@PathVariable Long id) {
        timesheetService.deleteTimesheet(id);
    }
}