package com.study_organizer.controller;

import com.study_organizer.dto.UserDTO;
import com.study_organizer.exception.CustomException;
import com.study_organizer.model.User;
import com.study_organizer.service.UserService;
import com.study_organizer.util.JwtUtil;
import com.study_organizer.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
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
    @Operation(summary = "Register a new user", description = "Endpoint to register a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Username already exists")
    })
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
    @Operation(summary = "Authenticate user", description = "Endpoint to authenticate a user and generate a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated, JWT token returned"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
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