package com.task.WorkVista.controller;

import com.task.WorkVista.dto.ProjectCreateDTO;
import com.task.WorkVista.dto.ProjectDTO;
import com.task.WorkVista.dto.ProjectWrapper;
import com.task.WorkVista.entity.Project;
import com.task.WorkVista.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ProjectDTO createProject(@RequestBody ProjectCreateDTO projectCreateDTO) {
        System.out.println("Creating project with manager ID: " + projectCreateDTO.getManagerId());
        return projectService.createProject(projectCreateDTO);
    }

    @GetMapping
    public List<ProjectDTO> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public Optional<ProjectDTO> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    public ProjectDTO updateProject(@PathVariable Long id, @RequestBody ProjectCreateDTO projectUpdateDTO) {
        return projectService.updateProjectFromDTO(id, projectUpdateDTO);
    }

    @PatchMapping("{projectId}")
    public ResponseEntity<String> updateProjectStatus(
            @PathVariable Long projectId,
            @RequestBody Map<String, String> updates) {
        projectService.statusPatch(projectId, updates);
        return ResponseEntity.ok("Project Status Updated");
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}