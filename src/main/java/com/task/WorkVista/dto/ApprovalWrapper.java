
package com.task.WorkVista.dto;

import com.task.WorkVista.entity.Approval;
import org.hibernate.Hibernate;

public class ApprovalWrapper {

    public static ApprovalDTO toDTO(Approval approval) {
        if (approval == null) return null;

        ApprovalDTO dto = new ApprovalDTO();
        dto.setId(approval.getId());

        if (approval.getTimesheet() != null && Hibernate.isInitialized(approval.getTimesheet())) {
            dto.setTimesheetId(approval.getTimesheet().getId());

            if (approval.getTimesheet().getTask() != null && Hibernate.isInitialized(approval.getTimesheet().getTask())) {
                dto.setTask(new SimpleTaskDTO(
                        approval.getTimesheet().getTask().getId(),
                        approval.getTimesheet().getTask().getName()
                ));
            }
        }

        if (approval.getManager() != null && Hibernate.isInitialized(approval.getManager())) {
            dto.setManager(new UserDTO(
                    approval.getManager().getId(),
                    approval.getManager().getName(),
                    approval.getManager().getEmail()
            ));
        }

        dto.setStatus(approval.getStatus());
        dto.setComments(approval.getComments());

        return dto;
    }
}