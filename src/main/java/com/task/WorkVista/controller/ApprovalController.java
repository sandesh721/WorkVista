
package com.task.WorkVista.controller;

import com.task.WorkVista.dto.ApprovalDTO;
import com.task.WorkVista.dto.ApprovalRequest;
import com.task.WorkVista.dto.ApprovalWrapper;
import com.task.WorkVista.entity.Approval;
import com.task.WorkVista.entity.User;
import com.task.WorkVista.service.ApprovalService;
import com.task.WorkVista.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

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
    public ApprovalDTO createApproval(@RequestBody Approval approval) {
        return ApprovalWrapper.toDTO(approvalService.saveApproval(approval));
    }

    @GetMapping
    public List<ApprovalDTO> getAllApprovals() {
        return approvalService.getAllApprovals().stream()
                .map(ApprovalWrapper::toDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ApprovalDTO updateApproval(@PathVariable Long id, @RequestBody Approval updatedApproval) {
        return ApprovalWrapper.toDTO(approvalService.updateApproval(id, updatedApproval));
    }

    @GetMapping("/{id}")
    public ApprovalDTO getApprovalById(@PathVariable Long id) {
        Approval approval = approvalService.getApprovalById(id).orElse(null);
        return ApprovalWrapper.toDTO(approval);
    }

    @GetMapping("/manager/{managerId}")
    public List<ApprovalDTO> getApprovalsByManager(@PathVariable Long managerId) {
        User manager = userService.getUserById(managerId);
        return approvalService.getApprovalsByManager(manager).stream()
                .map(ApprovalWrapper::toDTO)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteApproval(@PathVariable Long id) {
        approvalService.deleteApproval(id);
    }

    @PostMapping("/{id}/approve")
    public ApprovalDTO approveApproval(@PathVariable Long id, @RequestBody ApprovalRequest request) {
        return ApprovalWrapper.toDTO(approvalService.approveApproval(id, request.getManagerId()));
    }

    @PostMapping("/{id}/reject")
    public ApprovalDTO rejectApproval(@PathVariable Long id, @RequestBody ApprovalRequest request) {
        return ApprovalWrapper.toDTO(approvalService.rejectApproval(id, request.getManagerId(), request.getComments()));
    }
}