package com.task.WorkVista.controller;

import com.task.WorkVista.dto.*;
import com.task.WorkVista.entity.User;
import com.task.WorkVista.service.TaskService;
import com.task.WorkVista.service.TimesheetService;
import com.task.WorkVista.service.UserService;
import com.task.WorkVista.service.ApprovalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "http://localhost:5173")
public class ManagerController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TimesheetService timesheetService;

    @Autowired
    private ApprovalService approvalService;

    /**
     * ✅ Get all employees under a specific manager
     */
    @GetMapping("/team/{managerId}")
    public List<UserDetailDTO> getTeamByManager(@PathVariable Long managerId) {
        return userService.getUsersByManager(managerId).stream()
                .map(UserWrapper::toDetailDTO)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Get all tasks assigned under this manager's projects
     */
    @GetMapping("/tasks/{managerId}")
    public List<TaskDTO> getTasksByManager(@PathVariable Long managerId) {
        return taskService.getTasksByManager(managerId).stream()
                .map(TaskWrapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Get all pending approvals (for manager)
     */
    @GetMapping("/approvals/{managerId}/pending")
    public List<ApprovalDTO> getPendingApprovals(@PathVariable Long managerId) {
        return approvalService.getPendingApprovalsForManager(managerId).stream()
                .map(ApprovalWrapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * ✅ Get timesheets submitted by employees under this manager
     */
    @GetMapping("/timesheets/{managerId}")
    public List<TimesheetDTO> getTeamTimesheets(@PathVariable Long managerId) {
        return timesheetService.getTimesheetsByManager(managerId);
    }

    /**
     * ✅ Dashboard summary
     */
    @GetMapping("/dashboard/{managerId}")
    public ManagerDashboardDTO getManagerDashboard(@PathVariable Long managerId) {
        int totalTeamMembers = userService.getUsersByManager(managerId).size();
        int totalTasks = taskService.getTasksByManager(managerId).size();
        int pendingApprovals = approvalService.getPendingApprovalsForManager(managerId).size();

        return new ManagerDashboardDTO(totalTeamMembers, totalTasks, pendingApprovals);
    }
}