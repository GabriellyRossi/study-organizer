package com.study_organizer.repository;

import com.study_organizer.model.StudyMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudyMaterialRepository extends JpaRepository<StudyMaterial, Long> {
    List<StudyMaterial> findBySubject(String subject); // Método para buscar materiais por assunto
}

