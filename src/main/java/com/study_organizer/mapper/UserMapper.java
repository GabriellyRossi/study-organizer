package com.study_organizer.mapper;

import com.study_organizer.dto.UserDTO;
import com.study_organizer.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword()); // A senha será criptografada no service
        return user;
    }

    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        // Não incluímos a senha no DTO por questões de segurança
        return userDTO;
    }
}
