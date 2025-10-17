package com.task.WorkVista.dto;

import com.task.WorkVista.entity.Project;
import org.hibernate.Hibernate;

public class ProjectWrapper {
    public static ProjectDTO toDTO(Project project){
        if (project == null) return null;

        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStatus(project.getStatus() != null ? project.getStatus().name() : null);
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());

        if(project.getManager() != null && Hibernate.isInitialized(project.getManager())){
            ManagerDTO m = new ManagerDTO();
            m.setId(project.getManager().getId());
            m.setName(project.getManager().getName());
            m.setEmail(project.getManager().getEmail());
            dto.setManager(m);
        }

        return dto;
    }
}