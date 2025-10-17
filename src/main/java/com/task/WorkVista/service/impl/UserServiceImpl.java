package com.task.WorkVista.service.impl;

import com.task.WorkVista.entity.User;
import com.task.WorkVista.exception.ResourceAlreadyExistsException;
import com.task.WorkVista.exception.ResourceNotFoundException;
import com.task.WorkVista.repository.UserRepository;
import com.task.WorkVista.service.UserService;
import com.task.WorkVista.util.EntityReferenceResolver;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityReferenceResolver resolver;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EntityReferenceResolver resolver) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.resolver = resolver;
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists with email: " + user.getEmail());
        }
        if (user.getManager() != null && user.getManager().getId() != null) {
            User existingManager = userRepository.findById(user.getManager().getId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            user.setManager(existingManager);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByManager(Long managerId) {
        List<User> users = userRepository.findByManagerId(managerId);
        // Force initialization
        users.forEach(user -> {
            if (user.getManager() != null) {
                user.getManager().getName();
            }
        });
        return users;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        // Force initialization
        if (user.getManager() != null) {
            user.getManager().getName();
        }
        if (user.getTeam() != null && !user.getTeam().isEmpty()) {
            user.getTeam().forEach(teamMember -> teamMember.getName());
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        userOpt.ifPresent(user -> {
            if (user.getManager() != null) {
                user.getManager().getName();
            }
        });
        return userOpt;
    }

    @Override
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setRole(updatedUser.getRole());

        // Properly handle manager reference
        if (updatedUser.getManager() != null && updatedUser.getManager().getId() != null) {
            User manager = userRepository.findById(updatedUser.getManager().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + updatedUser.getManager().getId()));
            user.setManager(manager);
        } else {
            user.setManager(null);
        }

        User saved = userRepository.save(user);

        // Force initialization
        if (saved.getManager() != null) {
            saved.getManager().getName();
        }

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Force initialization
        users.forEach(user -> {
            if (user.getManager() != null) {
                user.getManager().getName();
            }
            if (user.getTeam() != null && !user.getTeam().isEmpty()) {
                user.getTeam().forEach(teamMember -> teamMember.getName());
            }
        });
        return users;
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}