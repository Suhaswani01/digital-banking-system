package com.bankingapp.service;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankingapp.dto.LoginRequest;
import com.bankingapp.dto.LoginResponse;
import com.bankingapp.dto.RegisterRequest;
import com.bankingapp.dto.RegisterResponse;
import com.bankingapp.entity.Role;
import com.bankingapp.entity.User;
import com.bankingapp.exception.RoleNotFoundException;
import com.bankingapp.exception.UserAlreadyExistsException;
import com.bankingapp.repositories.RoleRepository;
import com.bankingapp.repositories.UserRepository;
import com.bankingapp.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public RegisterResponse register(RegisterRequest request) {

        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

      
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Default role not found. Did RoleSeeder run?"));

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(customerRole))
                .build();

        User savedUser = userRepository.save(user);

        return RegisterResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .message("User registered successfully")
                .build();
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException(" User not found"));

        String token = jwtUtil.generateToken(user.getEmail());

        String role = user.getRoles().stream()
                .findFirst()
                .map(Role::getName)
                .orElse("ROLE_CUSTOMER");

        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .role(role)
                .build();
    }
}