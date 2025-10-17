package com.task.WorkVista.dto;

import com.task.WorkVista.entity.Task;
import org.hibernate.Hibernate;

public class TaskWrapper {

    public static TaskDTO toDTO(Task task) {
        if (task == null) return null;

        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setStatus(task.getStatus() != null ? task.getStatus().name() : null);
        dto.setPriority(task.getPriority() != null ? task.getPriority().name() : null);

        if (task.getAssignedTo() != null && Hibernate.isInitialized(task.getAssignedTo())) {
            dto.setAssignedTo(new UserDTO(
                    task.getAssignedTo().getId(),
                    task.getAssignedTo().getName(),
                    task.getAssignedTo().getEmail()
            ));
        }

        if (task.getManager() != null && Hibernate.isInitialized(task.getManager())) {
            dto.setManager(new UserDTO(
                    task.getManager().getId(),
                    task.getManager().getName(),
                    task.getManager().getEmail()
            ));
        }

        if (task.getProject() != null && Hibernate.isInitialized(task.getProject())) {
            dto.setProject(new SimpleProjectDTO(
                    task.getProject().getId(),
                    task.getProject().getName()
            ));
        }

        return dto;
    }
}