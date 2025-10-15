package com.task.WorkVista.repository;

import com.task.WorkVista.entity.Approval;
import com.task.WorkVista.entity.Timesheet;
import com.task.WorkVista.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    List<Approval> findByManager(User manager);
    @Query("SELECT a FROM Approval a WHERE a.manager.id = :managerId AND a.status = 'PENDING'")
    List<Approval> findPendingApprovalsByManager(Long managerId);

    Optional<Approval> findByTimesheet(Timesheet timesheet);
}