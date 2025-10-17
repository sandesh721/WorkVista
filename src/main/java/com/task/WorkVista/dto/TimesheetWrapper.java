
package com.task.WorkVista.dto;

import com.task.WorkVista.entity.Timesheet;
import org.hibernate.Hibernate;

public class TimesheetWrapper {
    public static TimesheetDTO toDTO(Timesheet ts) {
        if (ts == null) return null;

        TimesheetDTO dto = new TimesheetDTO();
        dto.setId(ts.getId());
        dto.setLocation(ts.getLocation());
        dto.setDate(ts.getDate());
        dto.setHours(ts.getHours());
        dto.setDescription(ts.getDescription());
        dto.setStatus(ts.getStatus() != null ? ts.getStatus().name() : null);

        // Safely access lazy-loaded relationships
        if (ts.getUser() != null && Hibernate.isInitialized(ts.getUser())) {
            dto.setUser(new UserDTO(
                    ts.getUser().getId(),
                    ts.getUser().getName(),
                    ts.getUser().getEmail()
            ));
        }

        if (ts.getManager() != null && Hibernate.isInitialized(ts.getManager())) {
            dto.setManager(new UserDTO(
                    ts.getManager().getId(),
                    ts.getManager().getName(),
                    ts.getManager().getEmail()
            ));
        }

        if (ts.getTask() != null && Hibernate.isInitialized(ts.getTask())) {
            dto.setTask(new SimpleTaskDTO(
                    ts.getTask().getId(),
                    ts.getTask().getName()
            ));
        }

        return dto;
    }
}