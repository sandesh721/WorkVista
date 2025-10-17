package com.task.WorkVista.controller;

import com.task.WorkVista.dto.AuthRequest;
import com.task.WorkVista.dto.AuthResponse;
import com.task.WorkVista.entity.User;
import com.task.WorkVista.repository.UserRepository;
import com.task.WorkVista.security.CustomUserDetailsService;
import com.task.WorkVista.security.JwtService;
import com.task.WorkVista.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final UserService userService;


    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          CustomUserDetailsService userDetailsService,
                          UserRepository userRepository,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        System.out.println(user);
        User savedUser = userService.saveUser(user);
        return  ResponseEntity.ok("User created Successfully");
    }
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        // 1. Authenticate user
        System.out.println("In controller");
        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        System.out.println("In controller");

        // 2. Load user details
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        // 3. Generate JWT
        String token = jwtService.generateToken(userDetails, user.getRole().name());

        return new AuthResponse(user.getId(), token, user.getName(), user.getRole());
    }
}