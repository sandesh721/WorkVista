package com.task.WorkVista.dto;


import lombok.Getter;

@Getter
public class ManagerDashboardDTO {
    private int totalTeamMembers;
    private int totalTasks;
    private int pendingApprovals;

    public ManagerDashboardDTO(int totalTeamMembers, int totalTasks, int pendingApprovals) {
        this.totalTeamMembers = totalTeamMembers;
        this.totalTasks = totalTasks;
        this.pendingApprovals = pendingApprovals;
    }

}

