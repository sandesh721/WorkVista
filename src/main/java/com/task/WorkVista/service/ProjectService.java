package com.task.WorkVista.service;

import com.task.WorkVista.entity.Project;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public interface ProjectService {
    Project saveProject(Project project);
    List<Project> getAllProjects();
    Optional<Project> getProjectById(Long id);

    Project updateProject(Long id, Project updatedProject);
    void statusPatch(Long projectId, Map<String, String> updates);
    void deleteProject(Long id);
}