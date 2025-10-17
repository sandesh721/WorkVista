package com.task.WorkVista.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDTO {
    private String name;
    private String description;
    private LocalDate dueDate;
    private String status;
    private String priority;
    private Long assignedTo;
    private Long manager;
    private Long project;
}
