
package com.task.WorkVista.service.impl;

import com.task.WorkVista.dto.ProjectDTO;
import com.task.WorkVista.dto.TaskCreateDTO;
import com.task.WorkVista.dto.TaskDTO;
import com.task.WorkVista.dto.TaskWrapper;
import com.task.WorkVista.entity.*;
import com.task.WorkVista.exception.ResourceNotFoundException;
import com.task.WorkVista.repository.ProjectRepository;
import com.task.WorkVista.repository.TaskRepository;
import com.task.WorkVista.repository.UserRepository;
import com.task.WorkVista.service.ProjectService;
import com.task.WorkVista.service.TaskService;
import com.task.WorkVista.util.EntityReferenceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskServiceImpl(TaskRepository taskRepository,UserRepository userRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EntityReferenceResolver resolver;

    @Override
    @Transactional
    public TaskDTO saveTask(TaskCreateDTO dto) {
        resolver.resolve(dto);
        Task task = new Task();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setPriority(TaskPriority.valueOf(dto.getPriority()));
        task.setStatus(TaskStatus.valueOf(dto.getStatus()));
        task.setDueDate(dto.getDueDate());

        if (dto.getManager() != null) {
            User manager = userRepository.findById(dto.getManager())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + dto.getManager()));
            task.setManager(manager);
        }if (dto.getAssignedTo() != null) {
            User user = userRepository.findById(dto.getAssignedTo())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getManager()));
            task.setAssignedTo(user);
        }
        if (dto.getProject() != null) {
            Project project = projectRepository.findById(dto.getProject())
                    .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + dto.getManager()));
            task.setProject(project);
        }

        Task saved = taskRepository.save(task);
        // Force initialization
        if (task.getAssignedTo() != null) {
            task.getAssignedTo().getName();
        }
        if (task.getManager() != null) {
            task.getManager().getName();
        }
        if (task.getProject() != null) {
            task.getProject().getName();
        }
        return TaskWrapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(task -> {
                    if (task.getAssignedTo() != null) task.getAssignedTo().getName();
                    if (task.getManager() != null) task.getManager().getName();
                    if (task.getProject() != null) task.getProject().getName();
                    return TaskWrapper.toDTO(task);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        // Force initialization
        if (task.getAssignedTo() != null) task.getAssignedTo().getName();
        if (task.getManager() != null) task.getManager().getName();
        if (task.getProject() != null) task.getProject().getName();

        return TaskWrapper.toDTO(task);
    }

    @Override
    @Transactional
    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Task not found with id: " + id));

        resolver.resolve(updatedTask);

        task.setName(updatedTask.getName());
        task.setDescription(updatedTask.getDescription());
        task.setAssignedTo(updatedTask.getAssignedTo());
        task.setProject(updatedTask.getProject());
        task.setManager(updatedTask.getManager());
        task.setStatus(updatedTask.getStatus());
        task.setPriority(updatedTask.getPriority());
        task.setDueDate(updatedTask.getDueDate());

        Task saved = taskRepository.save(task);

        // Force initialization
        if (saved.getAssignedTo() != null) {
            saved.getAssignedTo().getName();
        }
        if (saved.getManager() != null) {
            saved.getManager().getName();
        }
        if (saved.getProject() != null) {
            saved.getProject().getName();
        }

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByUser(User user) {
        return taskRepository.findByAssignedTo(user)
                .stream()
                .map(task -> {
                    if (task.getAssignedTo() != null) task.getAssignedTo().getName();
                    if (task.getManager() != null) task.getManager().getName();
                    if (task.getProject() != null) task.getProject().getName();
                    return TaskWrapper.toDTO(task);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getTasksByProject(Optional<ProjectDTO> project) {
        projectService.getProjectById(project.get().getId());
        List<Task> tasks = taskRepository.findByProject(project);
        // Force initialization
        tasks.forEach(task -> {
            if (task.getAssignedTo() != null) task.getAssignedTo().getName();
            if (task.getManager() != null) task.getManager().getName();
            if (task.getProject() != null) task.getProject().getName();
        });
        return tasks;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getTasksByManager(Long managerId) {
        List<Task> tasks = taskRepository.findByManagerId(managerId);
        // Force initialization
        tasks.forEach(task -> {
            if (task.getAssignedTo() != null) task.getAssignedTo().getName();
            if (task.getManager() != null) task.getManager().getName();
            if (task.getProject() != null) task.getProject().getName();
        });
        return tasks;
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
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
            Task saved = taskRepository.save(task);

            // Force initialization
            if (saved.getAssignedTo() != null) {
                saved.getAssignedTo().getName();
            }
            if (saved.getManager() != null) {
                saved.getManager().getName();
            }
            if (saved.getProject() != null) {
                saved.getProject().getName();
            }

            return saved;
        }
        return task;
    }
}