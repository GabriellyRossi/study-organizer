package com.study_organizer.controller;

import com.study_organizer.model.StudyMaterial;
import com.study_organizer.service.StudyMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.study_organizer.dto.StudyMaterialDTO;
import com.study_organizer.mapper.StudyMaterialMapper;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/materials")
@Tag(name = "Study Materials", description = "Endpoints for managing study materials")
public class StudyMaterialController {

    @Autowired
    private StudyMaterialService studyMaterialService;

    @Autowired
    private StudyMaterialMapper studyMaterialMapper;

    @GetMapping
    @Operation(summary = "Get all materials", description = "Retrieve a list of all study materials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Access denied")
    })
    public List<StudyMaterial> getAllMaterials() {
        return studyMaterialService.getAllMaterials();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get material by ID", description = "Retrieve a study material by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved material"),
            @ApiResponse(responseCode = "404", description = "Material not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Access denied")
    })
    public ResponseEntity<StudyMaterial> getMaterialById(@PathVariable Long id) {
        Optional<StudyMaterial> material = studyMaterialService.getMaterialById(id);
        return material.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/subject/{subject}")
    @Operation(summary = "Get materials by subject", description = "Retrieve a list of study materials by subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Access denied")
    })
    public List<StudyMaterial> getMaterialsBySubject(@PathVariable String subject) {
        return studyMaterialService.getMaterialsBySubject(subject);
    }

    @PostMapping
    @Operation(summary = "Create a new material", description = "Create a new study material")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Material created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Access denied")
    })
    @SecurityRequirement(name = "bearer-key") // Exige autenticação JWT
    public ResponseEntity<StudyMaterial> createMaterial(@Valid @RequestBody StudyMaterialDTO materialDTO) {
        StudyMaterial material = studyMaterialMapper.toEntity(materialDTO);
        StudyMaterial savedMaterial = studyMaterialService.saveMaterial(material);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMaterial);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a material", description = "Delete a study material by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Material deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Material not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Access denied")
    })
    @SecurityRequirement(name = "bearer-key") // Exige autenticação JWT
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        studyMaterialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}