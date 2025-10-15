package com.task.WorkVista.service.impl;

import com.task.WorkVista.entity.Project;
import com.task.WorkVista.entity.ProjectStatus;
import com.task.WorkVista.exception.ResourceNotFoundException;
import com.task.WorkVista.repository.ProjectRepository;
import com.task.WorkVista.service.ProjectService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Optional<Project> getProjectById(Long id) {
        return Optional.ofNullable(projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id)));
    }
    @Override
    public Project updateProject(Long id, Project updatedProject) {
        Project project = projectRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Project not found with id: "+ id));
        project.setName(updatedProject.getName());
        project.setDescription(updatedProject.getDescription());
        project.setStartDate(updatedProject.getStartDate());
        project.setEndDate(updatedProject.getEndDate());
        project.setStatus(updatedProject.getStatus());
        return projectRepository.save(project);
    }

    @Override
    public void statusPatch(Long projectId, Map<String, String> updates) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // Get new status from request body
        String newStatus = updates.get("status");
        if (newStatus != null) {
            try {
                ProjectStatus statusEnum = ProjectStatus.valueOf(newStatus);
                project.setStatus(statusEnum);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid project status: " + newStatus);
            }
        }

        projectRepository.save(project);
    }


    @Override
    public void deleteProject(Long id) {
        if(!projectRepository.existsById(id)){
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }
}