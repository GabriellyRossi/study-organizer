package com.study_organizer.controller;

import com.study_organizer.model.StudyMaterial;
import com.study_organizer.service.StudyMaterialService;
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
public class StudyMaterialController {

    @Autowired
    private StudyMaterialService studyMaterialService;

    @Autowired
    private StudyMaterialMapper studyMaterialMapper;

    @GetMapping
    public List<StudyMaterial> getAllMaterials() {
        return studyMaterialService.getAllMaterials();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyMaterial> getMaterialById(@PathVariable Long id) {
        Optional<StudyMaterial> material = studyMaterialService.getMaterialById(id);
        return material.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/subject/{subject}")
    public List<StudyMaterial> getMaterialsBySubject(@PathVariable String subject) {
        return studyMaterialService.getMaterialsBySubject(subject);
    }

    @PostMapping
    public ResponseEntity<StudyMaterial> createMaterial(@Valid @RequestBody StudyMaterialDTO materialDTO) {
        StudyMaterial material = studyMaterialMapper.toEntity(materialDTO);
        StudyMaterial savedMaterial = studyMaterialService.saveMaterial(material);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMaterial);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        studyMaterialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}