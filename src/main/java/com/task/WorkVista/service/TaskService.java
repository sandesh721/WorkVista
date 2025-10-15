package com.task.WorkVista.service;

import com.task.WorkVista.entity.Project;
import com.task.WorkVista.entity.Task;
import com.task.WorkVista.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public interface TaskService {
    Task saveTask(Task task);
    List<Task> getAllTasks();
    Optional<Task> getTaskById(Long id);

    Task updateTask(Long id, Task updatedTask);

    List<Task> getTasksByUser(User user);

    List<Task> getTasksByProject(Optional<Project> project);
    List<Task> getTasksByManager(Long managerId);

    void deleteTask(Long id);

    Task patchStatus(Long id, Map<String, String> updates);

}