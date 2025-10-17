package com.task.WorkVista.dto;

import com.task.WorkVista.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private UserDTO manager;  // Only basic info of manager
    private List<UserDTO> team;  // Only basic info of team members
}