package com.study_organizer.service;

import com.study_organizer.exception.CustomException;
import com.study_organizer.model.StudyMaterial;
import com.study_organizer.repository.StudyMaterialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudyMaterialServiceTest {

    @Mock
    private StudyMaterialRepository studyMaterialRepository;

    @InjectMocks
    private StudyMaterialService studyMaterialService;

    @Test
    public void testSaveMaterial() {
        StudyMaterial material = new StudyMaterial();
        material.setSubject("Java");
        material.setCourseName("Spring Boot Basics");

        when(studyMaterialRepository.save(any(StudyMaterial.class))).thenReturn(material);

        StudyMaterial savedMaterial = studyMaterialService.saveMaterial(material);

        assertNotNull(savedMaterial);
        assertEquals("Java", savedMaterial.getSubject());
        assertEquals("Spring Boot Basics", savedMaterial.getCourseName());
        verify(studyMaterialRepository, times(1)).save(material);
    }

    @Test
    public void testGetMaterialById() {
        StudyMaterial material = new StudyMaterial();
        material.setId(1L);
        material.setSubject("Java");

        when(studyMaterialRepository.findById(1L)).thenReturn(Optional.of(material));

        Optional<StudyMaterial> foundMaterial = studyMaterialService.getMaterialById(1L);

        assertTrue(foundMaterial.isPresent());
        assertEquals("Java", foundMaterial.get().getSubject());
        verify(studyMaterialRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetMaterialById_NotFound() {
        when(studyMaterialRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<StudyMaterial> foundMaterial = studyMaterialService.getMaterialById(1L);

        assertFalse(foundMaterial.isPresent());
        verify(studyMaterialRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetMaterialsBySubject() {
        StudyMaterial material1 = new StudyMaterial();
        material1.setSubject("Java");

        StudyMaterial material2 = new StudyMaterial();
        material2.setSubject("Java");

        when(studyMaterialRepository.findBySubject("Java")).thenReturn(List.of(material1, material2));

        List<StudyMaterial> materials = studyMaterialService.getMaterialsBySubject("Java");

        assertEquals(2, materials.size());
        verify(studyMaterialRepository, times(1)).findBySubject("Java");
    }

    @Test
    public void testDeleteMaterial() {
        doNothing().when(studyMaterialRepository).deleteById(1L);

        studyMaterialService.deleteMaterial(1L);

        verify(studyMaterialRepository, times(1)).deleteById(1L);
    }
}

