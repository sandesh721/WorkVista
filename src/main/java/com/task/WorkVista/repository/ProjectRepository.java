package com.task.WorkVista.repository;

import com.task.WorkVista.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}