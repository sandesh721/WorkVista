package com.task.WorkVista.service;

import com.task.WorkVista.dto.ProjectDTO;
import com.task.WorkVista.dto.TaskCreateDTO;
import com.task.WorkVista.dto.TaskDTO;
import com.task.WorkVista.entity.Task;
import com.task.WorkVista.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public interface TaskService {
    TaskDTO saveTask(TaskCreateDTO task);
    List<TaskDTO> getAllTasks();
    TaskDTO getTaskById(Long id);

    Task updateTask(Long id, Task updatedTask);

    List<TaskDTO> getTasksByUser(User user);

    List<Task> getTasksByProject(Optional<ProjectDTO> project);
    List<Task> getTasksByManager(Long managerId);

    void deleteTask(Long id);

    Task patchStatus(Long id, Map<String, String> updates);

}