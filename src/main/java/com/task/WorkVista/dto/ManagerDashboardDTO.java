package com.task.WorkVista.dto;


public class ManagerDashboardDTO {
    private int totalTeamMembers;
    private int totalTasks;
    private int pendingApprovals;

    public ManagerDashboardDTO(int totalTeamMembers, int totalTasks, int pendingApprovals) {
        this.totalTeamMembers = totalTeamMembers;
        this.totalTasks = totalTasks;
        this.pendingApprovals = pendingApprovals;
    }

    public int getTotalTeamMembers() {
        return totalTeamMembers;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public int getPendingApprovals() {
        return pendingApprovals;
    }
}

