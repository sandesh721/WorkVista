package com.task.WorkVista.controller;


import com.task.WorkVista.dto.UserDTO;
import com.task.WorkVista.dto.UserDetailDTO;
import com.task.WorkVista.dto.UserWrapper;
import com.task.WorkVista.entity.User;
import com.task.WorkVista.entity.Role;
import com.task.WorkVista.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDetailDTO> getAllUsers() {
        return UserWrapper.toDetailDTOList(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public UserDetailDTO getUserById(@PathVariable Long id) {
        return UserWrapper.toDetailDTO(userService.getUserById(id));
    }

    @GetMapping("/email/{email}")
    public UserDetailDTO getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email).orElse(null);
        return UserWrapper.toDetailDTO(user);
    }

    @GetMapping("/role/{role}")
    public List<UserDetailDTO> getUsersByRole(@PathVariable Role role) {
        List<User> users = userService.getAllUsers().stream()
                .filter(user -> user.getRole() == role)
                .toList();
        return UserWrapper.toDetailDTOList(users);
    }

    @PutMapping("/{id}")
    public UserDetailDTO updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return UserWrapper.toDetailDTO(userService.updateUser(id, updatedUser));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}