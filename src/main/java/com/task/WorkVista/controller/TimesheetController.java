package com.task.WorkVista.controller;

import com.task.WorkVista.entity.Timesheet;
import com.task.WorkVista.entity.User;
import com.task.WorkVista.service.TimesheetService;
import com.task.WorkVista.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/timesheets")
//@CrossOrigin(origins = "http://localhost:5173")
public class TimesheetController {

    private final TimesheetService timesheetService;

    public TimesheetController(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    @Autowired
    private UserService userService;

    @PostMapping
    public Timesheet createTimesheet(@RequestBody Timesheet timesheet) {
        return timesheetService.saveTimesheet(timesheet);
    }

    @GetMapping
    public List<Timesheet> getAllTimesheets() {
        return timesheetService.getAllTimesheets();
    }

    @GetMapping("/{id}")
    public Timesheet getTimesheetById(@PathVariable Long id) {
        return timesheetService.getTimesheetById(id).orElse(null);
    }
    @PutMapping("/{id}")
    public Timesheet updateTimesheet(@PathVariable Long id, @RequestBody Timesheet updatedTimesheet) {
        return timesheetService.updateTimesheet(id, updatedTimesheet);
    }
    @GetMapping("/user/{userId}")
    public List<Timesheet> getTimesheetsByUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return timesheetService.getTimesheetsByUser(user);
    }

    @GetMapping("/manager/{userId}")
    public List<Timesheet> getTimesheetsByManager(@PathVariable Long userId) {
//        User user = userService.getUserById(userId);
        return timesheetService.getTimesheetsByManager(userId);
    }

    @GetMapping("/range")
    public List<Timesheet> getTimesheetsBetweenDates(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        return timesheetService.getTimesheetsBetweenDates(start, end);
    }

    @DeleteMapping("/{id}")
    public void deleteTimesheet(@PathVariable Long id) {
        timesheetService.deleteTimesheet(id);
    }
}
