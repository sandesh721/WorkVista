package com.task.WorkVista.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetDTO {
    private Long id;
    private String location;
    private LocalDate date;
    private double hours;
    private String description;
    private String status;
    private UserDTO user;
    private UserDTO manager;
    private SimpleTaskDTO task;
}
