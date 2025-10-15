package com.task.WorkVista.repository;

import com.task.WorkVista.entity.Project;
import com.task.WorkVista.entity.Task;
import com.task.WorkVista.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(User user);

    List<Task> findByProject(Optional<Project> project);
    @Query("SELECT t FROM Task t WHERE t.manager.id = :managerId")
    List<Task> findByManagerId(Long managerId);
}