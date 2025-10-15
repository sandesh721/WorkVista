package com.task.WorkVista.service;

import com.task.WorkVista.entity.Approval;
import com.task.WorkVista.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface ApprovalService {
    Approval saveApproval(Approval approval);
    List<Approval> getAllApprovals();
    Optional<Approval> getApprovalById(Long id);
    void deleteApproval(Long id);
    List<Approval> getPendingApprovalsForManager(Long managerId);
    Approval updateApproval(Long id, Approval updatedApproval);
//    Approval approveApproval(Long id);
//    Approval rejectApproval(Long id, String comments);
    List<Approval> getApprovalsByManager(User manager);

    Approval approveApproval(Long timesheetId, Long managerId);
    Approval rejectApproval(Long timesheetId, Long managerId, String comments);
}