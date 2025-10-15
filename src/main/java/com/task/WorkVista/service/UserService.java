package com.task.WorkVista.service;

import com.task.WorkVista.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface UserService {
    User saveUser(User user);
    User getUserById(Long id);
    Optional<User> getUserByEmail(String email);

    User updateUser(Long id, User updatedUser);
    List<User> getUsersByManager(Long managerId);
    List<User> getAllUsers();
    void deleteUser(Long id);

}