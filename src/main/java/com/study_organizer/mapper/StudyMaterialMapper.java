package com.study_organizer.mapper;

import com.study_organizer.dto.StudyMaterialDTO;
import com.study_organizer.model.StudyMaterial;
import org.springframework.stereotype.Component;

@Component
public class StudyMaterialMapper {

    public StudyMaterial toEntity(StudyMaterialDTO materialDTO) {
        StudyMaterial material = new StudyMaterial();
        material.setSubject(materialDTO.getSubject());
        material.setCourseName(materialDTO.getCourseName());
        material.setAuthor(materialDTO.getAuthor());
        material.setDate(materialDTO.getDate());
        material.setLink(materialDTO.getLink());
        material.setAdditionalInfo(materialDTO.getAdditionalInfo());
        return material;

    }
}