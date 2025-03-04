package com.study_organizer.controller;

import com.study_organizer.model.StudyMaterial;
import com.study_organizer.service.StudyMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/materials")
public class StudyMaterialController {

    @Autowired
    private StudyMaterialService studyMaterialService;

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
    public StudyMaterial createMaterial(@RequestBody StudyMaterial material) {
        return studyMaterialService.saveMaterial(material);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        studyMaterialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}