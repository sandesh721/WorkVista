package com.task.WorkVista.dto;

import com.task.WorkVista.entity.Role;
import com.task.WorkVista.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserWrapper {

    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static UserDetailDTO toDetailDTO(User user) {
        if (user == null) return null;

        UserDetailDTO dto = new UserDetailDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        // Only include manager's basic info
        if (user.getManager() != null) {
            dto.setManager(toDTO(user.getManager()));
        }

        // Only include team members' basic info
        if (user.getTeam() != null) {
            dto.setTeam(user.getTeam().stream()
                    .map(UserWrapper::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static List<UserDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(UserWrapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<UserDetailDTO> toDetailDTOList(List<User> users) {
        return users.stream()
                .map(UserWrapper::toDetailDTO)
                .collect(Collectors.toList());
    }
}