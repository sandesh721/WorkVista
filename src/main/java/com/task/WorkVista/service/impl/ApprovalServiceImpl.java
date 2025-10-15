package com.task.WorkVista.service.impl;

import com.task.WorkVista.entity.*;
import com.task.WorkVista.exception.ResourceNotFoundException;
import com.task.WorkVista.repository.ApprovalRepository;
import com.task.WorkVista.repository.TimesheetRepository;
import com.task.WorkVista.repository.UserRepository;
import com.task.WorkVista.service.ApprovalService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final TimesheetRepository timesheetRepository;
    private final UserRepository userRepository;

    public ApprovalServiceImpl(ApprovalRepository approvalRepository, TimesheetRepository timesheetRepository, UserRepository userRepository) {
        this.approvalRepository = approvalRepository;
        this.timesheetRepository = timesheetRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Approval saveApproval(Approval approval) {
        return approvalRepository.save(approval);
    }

    @Override
    public List<Approval> getAllApprovals() {
        return approvalRepository.findAll();
    }

    @Override
    public Optional<Approval> getApprovalById(Long id) {
        return Optional.ofNullable(approvalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Approval not found with id: " + id)));
    }

    @Override
    public Approval updateApproval(Long id, Approval updatedApproval) {
        Approval ap = approvalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Approval not found with id: "+id));
        ap.setManager(updatedApproval.getManager());
        ap.setStatus(updatedApproval.getStatus());
        ap.setComments(updatedApproval.getComments());
        return approvalRepository.save(ap);
    }
    @Override
    public List<Approval> getPendingApprovalsForManager(Long managerId) {
        return approvalRepository.findPendingApprovalsByManager(managerId);
    }
    @Override
    public void deleteApproval(Long id) {
        approvalRepository.deleteById(id);
    }
    @Override
    public List<Approval> getApprovalsByManager(User manager) {
        return approvalRepository.findByManager(manager);
    }

    @Override
    public Approval approveApproval(Long timesheetId, Long managerId) {
        // Fetch the timesheet
        Timesheet timesheet = timesheetRepository.findById(timesheetId)
                .orElseThrow(() -> new RuntimeException("Timesheet not found"));

        // Update timesheet status
        timesheet.setStatus(TimesheetStatus.APPROVED);
        timesheetRepository.save(timesheet);

        // Check if an Approval already exists for this timesheet
        Approval approval = approvalRepository.findByTimesheet(timesheet)
                .orElseGet(() -> {
                    Approval newApproval = new Approval();
                    newApproval.setTimesheet(timesheet);
                    newApproval.setManager(userRepository.findById(managerId)
                            .orElseThrow(() -> new RuntimeException("Manager not found")));
                    return newApproval;
                });

        // Set approval status
        approval.setStatus(ApprovalStatus.APPROVED);
        approval.setComments(null); // no comments on approval

        return approvalRepository.save(approval);
    }

    @Override
    public Approval rejectApproval(Long timesheetId, Long managerId, String comments) {
        // Fetch the timesheet
        Timesheet timesheet = timesheetRepository.findById(timesheetId)
                .orElseThrow(() -> new RuntimeException("Timesheet not found"));

        // Update timesheet status
        timesheet.setStatus(TimesheetStatus.REJECTED);
        timesheetRepository.save(timesheet);

        // Check if an Approval already exists for this timesheet
        Approval approval = approvalRepository.findByTimesheet(timesheet)
                .orElseGet(() -> {
                    Approval newApproval = new Approval();
                    newApproval.setTimesheet(timesheet);
                    newApproval.setManager(userRepository.findById(managerId)
                            .orElseThrow(() -> new RuntimeException("Manager not found")));
                    return newApproval;
                });

        // Set approval status and comments
        approval.setStatus(ApprovalStatus.REJECTED);
        approval.setComments(comments);

        return approvalRepository.save(approval);
    }


}