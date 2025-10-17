package com.task.WorkVista.service;

import com.task.WorkVista.dto.ProjectCreateDTO;
import com.task.WorkVista.dto.ProjectDTO;
import com.task.WorkVista.entity.Project;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectService {
    Project saveProject(Project project);
    ProjectDTO createProject(ProjectCreateDTO projectCreateDTO);
    List<ProjectDTO> getAllProjects();
    Optional<ProjectDTO> getProjectById(Long id);
    Project updateProject(Long id, Project updatedProject);
    ProjectDTO updateProjectFromDTO(Long id, ProjectCreateDTO projectUpdateDTO);
    void statusPatch(Long projectId, Map<String, String> updates);
    void deleteProject(Long id);
}