package com.task.WorkVista.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class TimesheetCreateDTO {
    private String location;
    private LocalDate date;
    private double hours;
    private String description;
    private String status;   // "PENDING", "APPROVED", "REJECTED"
    private Long user;
    private Long manager;
    private Long task;

}
