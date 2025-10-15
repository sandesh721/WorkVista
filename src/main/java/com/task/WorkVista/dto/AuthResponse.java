package com.task.WorkVista.dto;

import com.task.WorkVista.entity.Role;

public class AuthResponse {
    private Long id;
    private String token;
    private String name;
    private String role;

    public AuthResponse(Long id, String token, String name, Role role) {
        this.id = id;
        this.token = token;
        this.name = name;
        this.role = String.valueOf(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getToken() { return token; }
}