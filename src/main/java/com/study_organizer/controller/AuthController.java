package com.study_organizer.controller;

import com.study_organizer.dto.UserDTO;
import com.study_organizer.model.User;
import com.study_organizer.service.UserService;
import com.study_organizer.util.JwtUtil;
import com.study_organizer.mapper.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
        // Verifica se o nome de usuário já existe
        if (userService.usernameExists(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Converte UserDTO para User e salva no banco de dados
        User user = userMapper.toEntity(userDTO);
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserDTO userDTO) {
        // Autentica o usuário
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
        );

        // Gera o token JWT
        final UserDetails userDetails = userService.loadUserByUsername(userDTO.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }
}