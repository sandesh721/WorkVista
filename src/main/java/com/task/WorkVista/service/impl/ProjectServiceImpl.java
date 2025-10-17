package com.task.WorkVista.service.impl;

import com.task.WorkVista.dto.ProjectCreateDTO;
import com.task.WorkVista.dto.ProjectDTO;
import com.task.WorkVista.dto.ProjectWrapper;
import com.task.WorkVista.entity.Project;
import com.task.WorkVista.entity.ProjectStatus;
import com.task.WorkVista.entity.User;
import com.task.WorkVista.exception.ResourceNotFoundException;
import com.task.WorkVista.repository.ProjectRepository;
import com.task.WorkVista.repository.UserRepository;
import com.task.WorkVista.service.ProjectService;
import com.task.WorkVista.util.EntityReferenceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    private EntityReferenceResolver resolver;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ProjectDTO createProject(ProjectCreateDTO dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());

        // Set status
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            project.setStatus(ProjectStatus.valueOf(dto.getStatus()));
        } else {
            project.setStatus(ProjectStatus.NOT_STARTED);
        }

        // Set manager if provided
        if (dto.getManagerId() != null) {
            User manager = userRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + dto.getManagerId()));
            project.setManager(manager);
        }

        Project saved = projectRepository.save(project);
        System.out.println("Project saved with manager: " + (saved.getManager() != null ? saved.getManager().getId() : "null"));

        // Force initialization
        if (saved.getManager() != null) {
            saved.getManager().getName();
            saved.getManager().getEmail();
        }

        return ProjectWrapper.toDTO(saved);
    }

    @Override
    @Transactional
    public Project saveProject(Project project) {
        resolver.resolve(project);
        Project saved = projectRepository.save(project);

        // Force initialization
        if (saved.getManager() != null) {
            saved.getManager().getName();
        }

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(project -> {
                    if (project.getManager() != null) {
                        project.getManager().getName();
                    }
                    return ProjectWrapper.toDTO(project);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectDTO> getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        // Force initialization
        if (project.getManager() != null) {
            project.getManager().getName();
        }

        return Optional.of(ProjectWrapper.toDTO(project));
    }

    @Override
    @Transactional
    public ProjectDTO updateProjectFromDTO(Long id, ProjectCreateDTO dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStartDate(dto.getStartDate());
        project.setEndDate(dto.getEndDate());

        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            project.setStatus(ProjectStatus.valueOf(dto.getStatus()));
        }

        // Update manager if provided
        if (dto.getManagerId() != null) {
            User manager = userRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + dto.getManagerId()));
            project.setManager(manager);
        } else {
            project.setManager(null);
        }

        Project saved = projectRepository.save(project);

        // Force initialization
        if (saved.getManager() != null) {
            saved.getManager().getName();
        }

        return ProjectWrapper.toDTO(saved);
    }

    @Override
    @Transactional
    public Project updateProject(Long id, Project updatedProject) {
        Project project = projectRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Project not found with id: "+ id));

        resolver.resolve(updatedProject);

        project.setName(updatedProject.getName());
        project.setDescription(updatedProject.getDescription());
        project.setStartDate(updatedProject.getStartDate());
        project.setEndDate(updatedProject.getEndDate());
        project.setStatus(updatedProject.getStatus());
        project.setManager(updatedProject.getManager());

        Project saved = projectRepository.save(project);

        // Force initialization
        if (saved.getManager() != null) {
            saved.getManager().getName();
        }

        return saved;
    }

    @Override
    @Transactional
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