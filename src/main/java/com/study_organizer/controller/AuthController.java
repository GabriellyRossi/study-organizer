package com.study_organizer.controller;

import com.study_organizer.dto.UserDTO;
import com.study_organizer.exception.CustomException;
import com.study_organizer.model.User;
import com.study_organizer.service.UserService;
import com.study_organizer.util.JwtUtil;
import com.study_organizer.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDTO userDTO) {
        if (userService.usernameExists(userDTO.getUsername())) {
            throw new CustomException("Username already exists", HttpStatus.BAD_REQUEST.value());
        }

        // Converte UserDTO para User e salva no banco de dados
        User user = userMapper.toEntity(userDTO);
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserDTO userDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
            );

            final UserDetails userDetails = userService.loadUserByUsername(userDTO.getUsername());
            final String token = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(token);
        } catch (UsernameNotFoundException ex) {
            throw new CustomException("Invalid username or password", HttpStatus.UNAUTHORIZED.value());
        }
    }
}