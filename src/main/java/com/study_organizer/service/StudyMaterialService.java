package com.study_organizer.service;

import com.study_organizer.model.StudyMaterial;
import com.study_organizer.repository.StudyMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudyMaterialService {

    @Autowired
    private StudyMaterialRepository studyMaterialRepository;

    public List<StudyMaterial> getAllMaterials() {
        return studyMaterialRepository.findAll();
    }

    public Optional<StudyMaterial> getMaterialById(Long id) {
        return studyMaterialRepository.findById(id);
    }

    public List<StudyMaterial> getMaterialsBySubject(String subject) {
        return studyMaterialRepository.findBySubject(subject);
    }

    public StudyMaterial saveMaterial(StudyMaterial material) {
        return studyMaterialRepository.save(material);
    }

    public void deleteMaterial(Long id) {
        studyMaterialRepository.deleteById(id);
    }
}
