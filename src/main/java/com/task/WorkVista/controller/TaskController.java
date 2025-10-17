package com.task.WorkVista.controller;

import com.task.WorkVista.dto.ProjectDTO;
import com.task.WorkVista.dto.TaskCreateDTO;
import com.task.WorkVista.dto.TaskDTO;
import com.task.WorkVista.dto.TaskWrapper;
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
    public TaskDTO createTask(@RequestBody TaskCreateDTO task) {
        return taskService.saveTask(task);
    }

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return TaskWrapper.toDTO(taskService.updateTask(id, updatedTask));
    }

    @PatchMapping("{id}/status")
    public TaskDTO updateStatus(@PathVariable Long id, @RequestBody Map<String, String> updates){
        return TaskWrapper.toDTO(taskService.patchStatus(id, updates));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/user/{userId}")
    public List<TaskDTO> getTasksByUserId(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return taskService.getTasksByUser(user);
    }

    @GetMapping("/project/{projectId}")
    public List<TaskDTO> getTasksByProject(@PathVariable Long projectId) {
        Optional<ProjectDTO> project = projectService.getProjectById(projectId);
        List<Task> tasks = taskService.getTasksByProject(project);
        return tasks.stream()
                .map(TaskWrapper::toDTO)
                .toList();
    }
}