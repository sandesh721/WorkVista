package com.task.WorkVista.service.impl;

import com.task.WorkVista.entity.Project;
import com.task.WorkVista.entity.Task;
import com.task.WorkVista.entity.TaskStatus;
import com.task.WorkVista.entity.User;
import com.task.WorkVista.exception.ResourceNotFoundException;
import com.task.WorkVista.repository.TaskRepository;
import com.task.WorkVista.service.ProjectService;
import com.task.WorkVista.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Autowired
    private ProjectService projectService;

    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return Optional.ofNullable(taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id)));
    }
    @Override
    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Task not found with id: " + id));
        task.setName(updatedTask.getName());
        task.setDescription(updatedTask.getDescription());
        task.setAssignedTo(updatedTask.getAssignedTo());
        task.setProject(updatedTask.getProject());
        task.setStatus(updatedTask.getStatus());
        task.setPriority(updatedTask.getPriority());
        return taskRepository.save(task);
    }
    @Override
    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByAssignedTo(user);
    }
    @Override
    public List<Task> getTasksByProject(Optional<Project> project) {
        projectService.getProjectById(project.get().getId());
        return taskRepository.findByProject(project);
    }
    @Override
    public List<Task> getTasksByManager(Long managerId) {
        return taskRepository.findByManagerId(managerId);
    }
    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Task patchStatus(Long id, Map<String, String> update) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task id does not exists"));
        String newStatus = update.get("status");

        if(newStatus!= null){
            try{
                TaskStatus statusEnum = TaskStatus.valueOf(newStatus);
                task.setStatus(statusEnum);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid project status: " + newStatus);
            }
            taskRepository.save(task);
        }
        return task;
    }
}