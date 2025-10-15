package com.task.WorkVista.controller;

import com.task.WorkVista.entity.Project;
import com.task.WorkVista.entity.Task;
import com.task.WorkVista.entity.User;
import com.task.WorkVista.service.ProjectService;
import com.task.WorkVista.service.TaskService;
import com.task.WorkVista.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }
    @PatchMapping("{id}/status")
    public Task updateStatus(@PathVariable Long id, @RequestBody Map<String, String> updates){
        return taskService.patchStatus(id, updates);
    }
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
    @GetMapping("/user/{userId}")
    public List<Task> getTasksByUserId(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return taskService.getTasksByUser(user);
    }

    // Get tasks under a project
    @GetMapping("/project/{projectId}")
    public List<Task> getTasksByProject(@PathVariable Long projectId) {
        Optional<Project> project = projectService.getProjectById(projectId);
        return taskService.getTasksByProject(project);
    }
}

