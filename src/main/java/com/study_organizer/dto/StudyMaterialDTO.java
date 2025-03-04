package com.study_organizer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class StudyMaterialDTO {

    @NotBlank(message = "É obrigatório incluir um assunto")
    @Size(max = 100, message = "Você ultrapassou o maximo de 100 characters")
    private String subject;

    @Size(max = 200, message = "Você ultrapassou o maximo de 200 characters")
    private String courseName;

    @Size(max = 100, message = "Você ultrapassou o maximo de 100 characters")
    private String author;

    private LocalDate date;

    @Size(max = 500, message = "Você ultrapassou o maximo de 500 characters")
    private String link;

    @Size(max = 1000, message = "Você ultrapassou o máximo de 1000 characteres")
    private String additionalInfo;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}