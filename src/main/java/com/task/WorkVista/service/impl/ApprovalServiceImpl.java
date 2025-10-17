
package com.task.WorkVista.service.impl;

import com.task.WorkVista.entity.*;
import com.task.WorkVista.exception.ResourceNotFoundException;
import com.task.WorkVista.repository.ApprovalRepository;
import com.task.WorkVista.repository.TimesheetRepository;
import com.task.WorkVista.repository.UserRepository;
import com.task.WorkVista.service.ApprovalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Approval saveApproval(Approval approval) {
        Approval saved = approvalRepository.save(approval);
        // Force initialization
        if (saved.getTimesheet() != null) {
            saved.getTimesheet().getId();
            if (saved.getTimesheet().getTask() != null) {
                saved.getTimesheet().getTask().getName();
            }
        }
        if (saved.getManager() != null) {
            saved.getManager().getName();
        }
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Approval> getAllApprovals() {
        List<Approval> approvals = approvalRepository.findAll();
        // Force initialization for each approval
        approvals.forEach(approval -> {
            if (approval.getTimesheet() != null) {
                approval.getTimesheet().getId();
                if (approval.getTimesheet().getTask() != null) {
                    approval.getTimesheet().getTask().getName();
                }
            }
            if (approval.getManager() != null) {
                approval.getManager().getName();
            }
        });
        return approvals;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Approval> getApprovalById(Long id) {
        Optional<Approval> approval = approvalRepository.findById(id);
        approval.ifPresent(a -> {
            if (a.getTimesheet() != null) {
                a.getTimesheet().getId();
                if (a.getTimesheet().getTask() != null) {
                    a.getTimesheet().getTask().getName();
                }
            }
            if (a.getManager() != null) {
                a.getManager().getName();
            }
        });
        return approval;
    }

    @Override
    @Transactional
    public Approval updateApproval(Long id, Approval updatedApproval) {
        Approval ap = approvalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Approval not found with id: "+id));
        ap.setManager(updatedApproval.getManager());
        ap.setStatus(updatedApproval.getStatus());
        ap.setComments(updatedApproval.getComments());
        Approval saved = approvalRepository.save(ap);

        // Force initialization
        if (saved.getTimesheet() != null) {
            saved.getTimesheet().getId();
            if (saved.getTimesheet().getTask() != null) {
                saved.getTimesheet().getTask().getName();
            }
        }
        if (saved.getManager() != null) {
            saved.getManager().getName();
        }
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Approval> getPendingApprovalsForManager(Long managerId) {
        List<Approval> approvals = approvalRepository.findPendingApprovalsByManager(managerId);
        approvals.forEach(approval -> {
            if (approval.getTimesheet() != null) {
                approval.getTimesheet().getId();
                if (approval.getTimesheet().getTask() != null) {
                    approval.getTimesheet().getTask().getName();
                }
            }
            if (approval.getManager() != null) {
                approval.getManager().getName();
            }
        });
        return approvals;
    }

    @Override
    public void deleteApproval(Long id) {
        approvalRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Approval> getApprovalsByManager(User manager) {
        List<Approval> approvals = approvalRepository.findByManager(manager);
        approvals.forEach(approval -> {
            if (approval.getTimesheet() != null) {
                approval.getTimesheet().getId();
                if (approval.getTimesheet().getTask() != null) {
                    approval.getTimesheet().getTask().getName();
                }
            }
            if (approval.getManager() != null) {
                approval.getManager().getName();
            }
        });
        return approvals;
    }

    @Override
    @Transactional
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

        Approval saved = approvalRepository.save(approval);

        // Force initialization
        if (saved.getTimesheet() != null) {
            saved.getTimesheet().getId();
            if (saved.getTimesheet().getTask() != null) {
                saved.getTimesheet().getTask().getName();
            }
        }
        if (saved.getManager() != null) {
            saved.getManager().getName();
        }

        return saved;
    }

    @Override
    @Transactional
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

        Approval saved = approvalRepository.save(approval);

        // Force initialization
        if (saved.getTimesheet() != null) {
            saved.getTimesheet().getId();
            if (saved.getTimesheet().getTask() != null) {
                saved.getTimesheet().getTask().getName();
            }
        }
        if (saved.getManager() != null) {
            saved.getManager().getName();
        }

        return saved;
    }
}