package com.study_organizer.integration;

import com.study_organizer.model.StudyMaterial;
import com.study_organizer.repository.StudyMaterialRepository;
import com.study_organizer.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Ativa o perfil "test"
public class StudyMaterialControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudyMaterialRepository studyMaterialRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    private String token;

    @BeforeEach
    public void setup() {
        studyMaterialRepository.deleteAll(); // Limpa o banco de dados antes de cada teste

        // Gera um token JWT para autenticação
        UserDetails userDetails = userDetailsService.loadUserByUsername("seuUsuario"); // Substitua pelo usuário válido
        token = jwtUtil.generateToken(userDetails);
    }

    @Test
    public void testCreateMaterial() throws Exception {
        String materialJson = """
            {
                "subject": "Java",
                "courseName": "Spring Boot Basics",
                "author": "John Doe",
                "date": "2023-10-01",
                "link": "https://example.com",
                "additionalInfo": "Intro to Spring Boot"
            }
            """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/materials")
                        .header("Authorization", "Bearer " + token) // Adiciona o token JWT no cabeçalho
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(materialJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subject").value("Java"))
                .andExpect(jsonPath("$.courseName").value("Spring Boot Basics"));
    }

    @Test
    public void testGetMaterialById() throws Exception {
        StudyMaterial material = new StudyMaterial();
        material.setSubject("Java");
        material.setCourseName("Spring Boot Basics");
        material.setAuthor("John Doe");
        material.setDate(LocalDate.of(2023, 10, 1));
        material.setLink("https://example.com");
        material.setAdditionalInfo("Intro to Spring Boot");

        StudyMaterial savedMaterial = studyMaterialRepository.save(material);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/materials/{id}", savedMaterial.getId())
                        .header("Authorization", "Bearer " + token)) // Adiciona o token JWT no cabeçalho
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject").value("Java"))
                .andExpect(jsonPath("$.courseName").value("Spring Boot Basics"));
    }

    @Test
    public void testGetMaterialById_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/materials/{id}", 999L)
                        .header("Authorization", "Bearer " + token)) // Adiciona o token JWT no cabeçalho
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetMaterialsBySubject() throws Exception {
        StudyMaterial material1 = new StudyMaterial();
        material1.setSubject("Java");
        material1.setCourseName("Spring Boot Basics");

        StudyMaterial material2 = new StudyMaterial();
        material2.setSubject("Java");
        material2.setCourseName("Advanced Spring Boot");

        studyMaterialRepository.save(material1);
        studyMaterialRepository.save(material2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/materials/subject/{subject}", "Java")
                        .header("Authorization", "Bearer " + token)) // Adiciona o token JWT no cabeçalho
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].subject").value("Java"))
                .andExpect(jsonPath("$[1].subject").value("Java"));
    }

    @Test
    public void testDeleteMaterial() throws Exception {
        StudyMaterial material = new StudyMaterial();
        material.setSubject("Java");
        material.setCourseName("Spring Boot Basics");

        StudyMaterial savedMaterial = studyMaterialRepository.save(material);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/materials/{id}", savedMaterial.getId())
                        .header("Authorization", "Bearer " + token)) // Adiciona o token JWT no cabeçalho
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/materials/{id}", savedMaterial.getId())
                        .header("Authorization", "Bearer " + token)) // Adiciona o token JWT no cabeçalho
                .andExpect(status().isNotFound());
    }
}
