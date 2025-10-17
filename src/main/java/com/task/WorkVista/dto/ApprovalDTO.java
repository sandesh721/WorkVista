package com.task.WorkVista.dto;

import com.task.WorkVista.entity.ApprovalStatus;
import com.task.WorkVista.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalDTO {
    private Long id;
    private Long timesheetId;
    private UserDTO manager;
    private ApprovalStatus status;
    private String comments;
    private SimpleTaskDTO task;

}
