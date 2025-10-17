package com.task.WorkVista.dto;

import com.task.WorkVista.entity.Task;
import lombok.*;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO{
    private Long id;
    private String name;
    private String description;
    private LocalDate dueDate;
    private String status;
    private String priority;
    private UserDTO assignedTo;
    private UserDTO manager;
    private SimpleProjectDTO project;

    public TaskDTO(Task task) {
    }
}
