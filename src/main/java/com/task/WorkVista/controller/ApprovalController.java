package com.task.WorkVista.controller;

import com.task.WorkVista.dto.ApprovalRequest;
import com.task.WorkVista.dto.RejectRequest;
import com.task.WorkVista.entity.Approval;
import com.task.WorkVista.entity.User;
import com.task.WorkVista.service.ApprovalService;
import com.task.WorkVista.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/approvals")
@CrossOrigin(origins = "http://localhost:5173")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @Autowired
    private UserService userService;
    @PostMapping
    public Approval createApproval(@RequestBody Approval approval) {
        return approvalService.saveApproval(approval);
    }

    @GetMapping
    public List<Approval> getAllApprovals() {
        return approvalService.getAllApprovals();
    }

    @PutMapping("/{id}")
    public Approval updateApproval(@PathVariable Long id, @RequestBody Approval updatedApproval) {
        return approvalService.updateApproval(id, updatedApproval);
    }

    @GetMapping("/{id}")
    public Approval getApprovalById(@PathVariable Long id) {
        return approvalService.getApprovalById(id).orElse(null);
    }

    @GetMapping("/manager/{managerId}")
    public List<Approval> getApprovalsByManager(@PathVariable Long managerId) {
        User manager = userService.getUserById(managerId);
        return approvalService.getApprovalsByManager(manager);
    }
        @DeleteMapping("/{id}")
    public void deleteApproval(@PathVariable Long id) {
        approvalService.deleteApproval(id);
    }
    @PostMapping("/{id}/approve")
    public Approval approveApproval(@PathVariable Long id, @RequestBody ApprovalRequest request) {
        return approvalService.approveApproval(id, request.getManagerId());
    }

    @PostMapping("/{id}/reject")
    public Approval rejectApproval(@PathVariable Long id, @RequestBody ApprovalRequest request) {
        return approvalService.rejectApproval(id, request.getManagerId(), request.getComments());
    }

}
